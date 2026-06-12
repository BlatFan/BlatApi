package ru.blatfan.blatapi.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.joml.Matrix4f;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.common.multiblock.AbstractMultiblock;
import ru.blatfan.blatapi.common.multiblock.Multiblock;
import ru.blatfan.blatapi.common.multiblock.matcher.DisplayOnlyMatcher;
import ru.blatfan.blatapi.common.multiblock.matcher.Matchers;
import ru.blatfan.blatapi.utils.ClientTicks;
import ru.blatfan.blatapi.utils.collection.Text;
import ru.blatfan.blatapi.utils.gui_utils.GuiRenderUtil;
import ru.blatfan.blatapi.utils.gui_utils.GuiTextUtil;

import java.awt.*;
import java.util.*;
import java.util.function.Function;

public class MultiblockPreviewRenderer {
    public static boolean hasMultiblock;
    
    public static Map<BlockPos, BlockEntity> blockEntityCache = new Object2ObjectOpenHashMap<>();
    public static Set<BlockEntity> erroredBlockEntities = Collections.newSetFromMap(new WeakHashMap<>());
    @Getter
    private static Multiblock multiblock;
    private static Component name;
    private static BlockPos pos;
    @Getter
    private static boolean isAnchored;
    private static Rotation facingRotation;
    private static Function<BlockPos, BlockPos> offsetApplier;
    private static int blocks, blocksDone, airFilled;
    private static int timeComplete;
    private static BlockState lookingState;
    private static BlockPos lookingPos;
    private static MultiBufferSource.BufferSource buffers = null;
    
    public static void setMultiblock(Multiblock multiblock, Component name, boolean flip) {
        setMultiblock(multiblock, name, flip, pos -> pos);
    }
    
    public static void setMultiblock(Multiblock multiblock, Component name, boolean flip, Function<BlockPos, BlockPos> offsetApplier) {
        if (flip && hasMultiblock) {
            hasMultiblock = false;
        } else {
            MultiblockPreviewRenderer.multiblock = multiblock;
            MultiblockPreviewRenderer.blockEntityCache = new HashMap<>();
            MultiblockPreviewRenderer.erroredBlockEntities = Collections.newSetFromMap(new WeakHashMap<>());
            MultiblockPreviewRenderer.name = name;
            MultiblockPreviewRenderer.offsetApplier = offsetApplier;
            pos = null;
            hasMultiblock = multiblock != null;
            isAnchored = false;
        }
    }
    
    public static void onRenderHUD(GuiGraphics gui, float partialTicks) {
        if (hasMultiblock) {
            int waitTime = 40;
            int fadeOutSpeed = 4;
            int fullAnimTime = waitTime + 10;
            float animTime = timeComplete + (timeComplete == 0 ? 0 : partialTicks);
            
            if (animTime > fullAnimTime) {
                hasMultiblock = false;
                return;
            }
            
            gui.pose().pushPose();
            gui.pose().translate(0, -Math.max(0, animTime - waitTime) * fadeOutSpeed, 0);
            
            Minecraft mc = Minecraft.getInstance();
            int x = mc.getWindow().getGuiScaledWidth() / 2;
            int y = 12;
            
            GuiTextUtil.drawString(gui, mc.font, name, x - mc.font.width(name) / 2.0F, y, 0xFFFFFF, false);
            
            int width = 180;
            int height = 9;
            int left = x - width / 2;
            int top = y + 10;
            
            if (timeComplete > 0) {
                String s = Text.create("multiblock.blatapi.complete").getString();
                gui.pose().pushPose();
                gui.pose().translate(0, Math.min(height + 5, animTime), 0);
                gui.drawString(mc.font, s, (int) (x - mc.font.width(s) / 2.0F), top + height - 10, 0x00FF00, false);
                gui.pose().popPose();
            }
            
            gui.fill(left - 1, top - 1, left + width + 1, top + height + 1, 0xFF000000);
            drawGradientRect(gui, left, top, left + width, top + height, 0xFF666666, 0xFF555555);
            
            float fract = (float) blocksDone / Math.max(1, blocks);
            int progressWidth = (int) ((float) width * fract);
            int color = Mth.hsvToRgb(fract / 3.0F, 1.0F, 1.0F) | 0xFF000000;
            int color2 = new Color(color).darker().getRGB();
            drawGradientRect(gui, left, top, left + progressWidth, top + height, color, color2);
            
            if (!isAnchored) {
                String s = Text.create("multiblock.blatapi.not_anchored").getString();
                gui.drawString(mc.font, s, (int) (x - mc.font.width(s) / 2.0F), top + height + 8, 0xFFFFFF, false);
            } else {
                if (lookingState != null) {
                    // try-catch around here because the state isn't necessarily present in the world in this instance,
                    // which isn't really expected behavior for getPickBlock
                    try {
                        Block block = lookingState.getBlock();
                        ItemStack stack = block.getCloneItemStack(mc.level, lookingPos, lookingState);
                        
                        if (!stack.isEmpty()) {
                            gui.drawString(mc.font, stack.getHoverName(), left + 20, top + height + 8, 0xFFFFFF, false);
                            
                            gui.renderItem(stack, left, top + height + 2);
                        }
                    } catch (Exception ignored) {
                    }
                }
                
                if (timeComplete == 0) {
                    color = 0xFFFFFF;
                    int posx = left + width;
                    int posy = top + height + 2;
                    float mult = 1;
                    String progress = blocksDone + "/" + blocks;
                    
                    if (blocksDone == blocks && airFilled > 0) {
                        progress = Text.create("multiblock.blatapi.remove_blocks").getString();
                        color = 0xDA4E3F;
                        mult *= 2;
                        posx -= width / 2;
                        posy += 2;
                    }
                    
                    gui.drawString(mc.font, progress, (int) (posx - mc.font.width(progress) / mult), posy, color, true);
                }
            }
            
            gui.pose().popPose();
        }
    }
    
    public static void onRenderLevelLastEvent(PoseStack poseStack) {
        if (hasMultiblock && multiblock != null)
            renderMultiblock(Minecraft.getInstance().level, poseStack);
    }
    
    public static void anchorTo(BlockPos target, Rotation rot) {
        pos = target;
        facingRotation = rot;
        isAnchored = true;
    }
    
    public static InteractionResult onPlayerInteract(Player player, Level world, InteractionHand hand, BlockHitResult hit) {
        if (hasMultiblock && !isAnchored && player == Minecraft.getInstance().player) {
            anchorTo(hit.getBlockPos(), getRotation(player));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
    
    public static void onClientTick() {
        if (GuideClient.level == null) {
            hasMultiblock = false;
        } else if (isAnchored && blocks == blocksDone && airFilled == 0) {
            timeComplete++;
            if (timeComplete == 14) {
                GuideClient.mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F));
            }
        } else {
            timeComplete = 0;
        }
    }
    
    public static int buildMultiblock(ServerLevel level){
        if(multiblock==null) return 0 ;
        Pair<BlockPos, Collection<Multiblock.SimulateResult>> sim = multiblock.simulate(level, getStartPos().offset(0, 1, 0), getFacingRotation(), false, false);
        for (Multiblock.SimulateResult r : sim.getSecond()) {
            if (!r.getStateMatcher().equals(Matchers.ANY) && r.getStateMatcher().getType() != DisplayOnlyMatcher.TYPE)
                if (!r.test(level, facingRotation)) {
                    BlockState renderState = r.getStateMatcher().getDisplayedState(ClientTicks.ticks).rotate(facingRotation);
                    level.setBlock(r.getWorldPosition(), renderState, Block.UPDATE_ALL);
                }
        }
        MultiblockPreviewRenderer.setMultiblock(null, Component.empty(), false);
        return 1;
    }
    
    public static void renderMultiblock(Level level, PoseStack ms) {
        Minecraft mc = Minecraft.getInstance();
        if (!isAnchored) {
            facingRotation = getRotation(mc.player);
            if (mc.hitResult instanceof BlockHitResult)
                pos = ((BlockHitResult) mc.hitResult).getBlockPos();
        } else if (pos.distToCenterSqr(mc.player.position()) > 64 * 64)
            return;
        
        if (pos == null)
            return;
        if (multiblock.isSymmetrical())
            facingRotation = Rotation.NONE;
        
        multiblock.setLevel(level);
        
        EntityRenderDispatcher erd = mc.getEntityRenderDispatcher();
        double renderPosX = erd.camera.getPosition().x();
        double renderPosY = erd.camera.getPosition().y();
        double renderPosZ = erd.camera.getPosition().z();
        ms.pushPose();
        ms.translate(-renderPosX, -renderPosY, -renderPosZ);
        
        buffers = GhostBuffers.create(mc.renderBuffers().bufferSource(), 0.75f+0.25f*(float) Math.sin(ClientTicks.ticks/20f));
        
        BlockPos checkPos = null;
        if (mc.hitResult instanceof BlockHitResult blockRes)
            checkPos = blockRes.getBlockPos().relative(blockRes.getDirection());
        
        blocks = blocksDone = airFilled = 0;
        lookingState = null;
        lookingPos = checkPos;
        
        Pair<BlockPos, Collection<Multiblock.SimulateResult>> sim = multiblock.simulate(level, getStartPos(), getFacingRotation(), true, false);
        for (Multiblock.SimulateResult r : sim.getSecond()) {
            if (r.getWorldPosition().equals(checkPos))
                lookingState = r.getStateMatcher().getDisplayedState(ClientTicks.ticks);
            
            if (!r.getStateMatcher().equals(Matchers.ANY) && r.getStateMatcher().getType() != DisplayOnlyMatcher.TYPE) {
                boolean air = !r.getStateMatcher().countsTowardsTotalBlocks();
                if (!air) blocks++;
                
                if (!r.test(level, facingRotation)) {
                    BlockState renderState = r.getStateMatcher().getDisplayedState(ClientTicks.ticks).rotate(facingRotation);
                    renderBlock(renderState, r.getWorldPosition(), ms);
                    
                    if (renderState.getBlock() instanceof EntityBlock eb) {
                        var be = blockEntityCache.computeIfAbsent(r.getWorldPosition().immutable(), p -> eb.newBlockEntity(p, renderState));
                        if (be != null && !erroredBlockEntities.contains(be)) {
                            be.setLevel(mc.level);
                            be.setBlockState(renderState);
                            
                            ms.pushPose();
                            var bePos = r.getWorldPosition();
                            ms.translate(bePos.getX(), bePos.getY(), bePos.getZ());
                            ms.translate(0.25, 0.25, 0.25);
                            ms.scale(0.5f, 0.5f, 0.5f);
                            
                            try {
                                BlockEntityRenderer<BlockEntity> renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(be);
                                if (renderer != null)
                                    renderer.render(be, ClientTicks.partialTicks, ms, buffers, GuiRenderUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
                            } catch (Exception e) {
                                erroredBlockEntities.add(be);
                                BlatApi.LOGGER.error("Error rendering block entity", e);
                            }
                            ms.popPose();
                        }
                    }
                    
                    if (air) airFilled++;
                } else if (!air) blocksDone++;
            }
        }
        
        buffers.endBatch();
        ms.popPose();
        
        if (!isAnchored) blocks = blocksDone = 0;
    }
    
    public static void renderBlock(BlockState state, BlockPos pos, PoseStack ms) {
        if (pos != null) {
            ms.pushPose();
            ms.translate(pos.getX(), pos.getY(), pos.getZ());
            ms.translate(0.25, 0.25, 0.25);
            ms.scale(0.5f, 0.5f, 0.5f);
            
            var br = GuideClient.mc.getBlockRenderer();
            
            if (state.getBlock() == Blocks.AIR)
                state = Blocks.BEDROCK.defaultBlockState();
            
            var fluidState = state.getFluidState();
            if (!fluidState.isEmpty()) {
                var layer = ItemBlockRenderTypes.getRenderLayer(fluidState);
                var buffer = buffers.getBuffer(layer);
                br.renderLiquid(pos, getMultiblock(), new FluidBlockVertexConsumer(buffer, ms, pos), state, fluidState);
            }
            
            br.renderSingleBlock(state, ms, buffers, GuiRenderUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
            
            ms.popPose();
        }
    }
    
    public static Rotation getFacingRotation() {
        return multiblock.isSymmetrical() ? Rotation.NONE : facingRotation;
    }
    
    public static BlockPos getStartPos() {
        return offsetApplier.apply(pos);
    }
    
    private static void drawGradientRect(GuiGraphics gui, int left, int top, int right, int bottom, int startColor, int endColor) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f mat = gui.pose().last().pose();
        bufferbuilder.vertex(mat, right, top, 0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.vertex(mat, left, top, 0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.vertex(mat, left, bottom, 0).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.vertex(mat, right, bottom, 0).color(f5, f6, f7, f4).endVertex();
        tessellator.end();
        RenderSystem.disableBlend();
    }
    
    /**
     * Returns the Rotation of a multiblock structure based on the given entity's facing direction.
     */
    private static Rotation getRotation(Entity entity) {
        return AbstractMultiblock.rotationFromFacing(entity.getDirection());
    }
}