package ru.blatfan.blatapi.common.guide_book.pages;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.client.render.FluidBlockVertexConsumer;
import ru.blatfan.blatapi.client.render.MultiblockPreviewRenderer;
import ru.blatfan.blatapi.common.GuideManager;
import ru.blatfan.blatapi.common.guide_book.GuideBookPage;
import ru.blatfan.blatapi.common.multiblock.Multiblock;
import ru.blatfan.blatapi.utils.ClientTicks;
import ru.blatfan.blatapi.utils.ColorHelper;
import ru.blatfan.blatapi.utils.GuiUtil;
import ru.blatfan.blatapi.utils.collection.SplitText;
import ru.blatfan.blatapi.utils.collection.Text;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MultiblockPage extends GuideBookPage {
    public static final ResourceLocation TYPE = BlatApi.loc("multiblock");
    private final ResourceLocation multiblock;
    private final float scale;
    private final int buttonY;
    private final Color color;
    private final SplitText texts;
    
    public MultiblockPage(Component title, Color titleColor, boolean separator, ResourceLocation multiblock, float scale, int buttonY, Color color, List<Component> text) {
        super(title, titleColor, separator);
        this.multiblock = multiblock;
        this.scale = scale;
        this.buttonY = buttonY;
        this.color = color;
        
        if(!text.isEmpty())
            this.texts = TextPage.splitText(text, GuideClient.pageWidth-4,GuideClient.pageHeight - 100);
        else this.texts=new SplitText(1);
    }
    
    public static GuideBookPage json(JsonObject jsonObject) {
        List<Component> text = new ArrayList<>();
        if(jsonObject.has("text"))
            for(JsonElement element : jsonObject.getAsJsonArray("text"))
                text.add(Text.create(element.getAsString()));
        return new MultiblockPage(
            Text.create(jsonObject.get("title").getAsString()),
            jsonObject.has("title_color") ? ColorHelper.getColor(jsonObject.get("title_color").getAsString()) : Color.WHITE,
            jsonObject.has("separator") && jsonObject.get("separator").getAsBoolean(),
            ResourceLocation.tryParse(jsonObject.get("multiblock").getAsString()),
            jsonObject.has("scale") ? jsonObject.get("scale").getAsFloat() : 1f,
            jsonObject.get("buttonY").getAsInt(),
            jsonObject.has("color") ? ColorHelper.getColor(jsonObject.get("color").getAsString()) : Color.WHITE, text
        );
    }
    
    public Multiblock getMultiblock(){
        return GuideManager.getMultiblock(multiblock);
    }
    
    @Override
    protected int height() {
        return buttonY+7+texts.height();
    }
    
    @Override
    protected void render(GuiGraphics gui, int x, int y, int mX, int mY, float partialTick) {
        blockEntityCache.clear();
        blockEntityCache.putAll(MultiblockPreviewRenderer.blockEntityCache);
        erroredBlockEntities.clear();
        erroredBlockEntities.addAll(MultiblockPreviewRenderer.erroredBlockEntities);
        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.translate(x, y, 0);
        pose.scale(scale, scale, scale);
        this.renderMultiblock(gui);
        pose.popPose();
        
        Multiblock preview = MultiblockPreviewRenderer.getMultiblock();
        _buttonX=x+104;
        _buttonY=y+buttonY;
        if(preview==null || !preview.getId().equals(multiblock))
            gui.blit(GuideClient.guideBookData.getTexture(), _buttonX, _buttonY, 22, 70, 11, 7);
        else
            gui.blit(GuideClient.guideBookData.getTexture(), _buttonX, _buttonY, 22, 77, 11, 7);
        
        for(int i=0; i<texts.size(); i++) {
            String c = texts.get(i);
            int ty = _buttonY + 7 + (GuideClient.font.lineHeight * i);
            GuiUtil.drawScaledString(gui, c, x + 4, ty, color, texts.scale());
        }
    }
    
    private int _buttonX, _buttonY;
    
    @Override
    public boolean mouseClicked(double mX, double mY, int button) {
        Multiblock preview = MultiblockPreviewRenderer.getMultiblock();
        if(mX>=_buttonX && mX<=_buttonX+11 && mY>=_buttonY&&mY<=_buttonY+7 && button==0)
            if(preview==null || !preview.getId().equals(multiblock)) {
                Minecraft.getInstance().setScreen(null);
                MultiblockPreviewRenderer.setMultiblock(getMultiblock(), Text.create(getTitle()).withColor(getTitleColor()), false);
                return true;
            }
            else {
                MultiblockPreviewRenderer.setMultiblock(null, Text.create(getTitle()).withColor(getTitleColor()), false);
                return true;
            }
        return false;
    }
    
    private final Map<BlockPos, BlockEntity> blockEntityCache = new Object2ObjectOpenHashMap<>();
    private final Set<BlockEntity> erroredBlockEntities = Collections.newSetFromMap(new WeakHashMap<>());
    protected Pair<BlockPos, Collection<Multiblock.SimulateResult>> multiblockSimulation;
    
    private void renderMultiblock(GuiGraphics gui) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        float time = ClientTicks.ticks;
        
        BlockPos pos = BlockPos.ZERO;
        Rotation facingRotation = Rotation.NONE;
        
        getMultiblock().setLevel(level);
        
        Vec3i size = getMultiblock().getSize();
        int sizeX = size.getX();
        int sizeY = size.getY();
        int sizeZ = size.getZ();
        float maxX = 70;
        float maxY = 70;
        float diag = (float) Math.sqrt(sizeX * sizeX + sizeZ * sizeZ);
        float scaleX = maxX / diag;
        float scaleY = maxY / sizeY;
        float scale = -Math.min(scaleX, scaleY);
        
        PoseStack pose = gui.pose();
        pose.pushPose();
        
        pose.translate(maxX, maxY/2, 100);
        pose.scale(scale, scale, scale);
        pose.translate(-(float) sizeX / 2, -(float) sizeY / 2, 0);
        
        
        // Initial eye pos somewhere off in the distance in the -Z direction
        Vector4f eye = new Vector4f(0, 0, -100, 1);
        Matrix4f rotMat = new Matrix4f();
        rotMat.identity();
        
        // For each GL rotation done, track the opposite to keep the eye pos accurate
        pose.mulPose(Axis.XP.rotationDegrees(-30F));
        rotMat.rotate(Axis.XP.rotationDegrees(30));
        
        float offX = (float) -sizeX / 2;
        float offZ = (float) -sizeZ / 2 + 1;
        
        pose.translate(-offX, 0, -offZ);
        pose.mulPose(Axis.YP.rotationDegrees(time));
        rotMat.rotate(Axis.YP.rotationDegrees(-time));
        pose.mulPose(Axis.YP.rotationDegrees(45));
        rotMat.rotate(Axis.YP.rotationDegrees(-45));
        pose.translate(offX, 0, offZ);
        
        // Finally apply the rotations
        rotMat.transform(eye);
        eye.div(eye.w);
        
        
        var buffers = mc.renderBuffers().bufferSource();
        
        BlockPos checkPos = null;
        if (mc.hitResult instanceof BlockHitResult blockRes) {
            checkPos = blockRes.getBlockPos().relative(blockRes.getDirection());
        }
        
        pose.pushPose();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        pose.translate(0, 0, -1);
        
        if(multiblockSimulation==null)multiblockSimulation = getMultiblock().simulate(level, pos, facingRotation, false, false);
        for (Multiblock.SimulateResult r : multiblockSimulation.getSecond()) {
            float alpha = 0.3F;
            if (r.getWorldPosition().equals(checkPos)) {
                alpha = 0.6F + (float) (Math.sin(ClientTicks.total * 0.3F) + 1F) * 0.1F;
            }
            
            BlockState renderState = r.getStateMatcher().getDisplayedState(ClientTicks.ticks).rotate(facingRotation);
            
            this.renderBlock(buffers, level, renderState, r.getWorldPosition(), alpha, pose);
            
            if (renderState.getBlock() instanceof EntityBlock eb) {
                var be = this.blockEntityCache.computeIfAbsent(r.getWorldPosition().immutable(), p -> eb.newBlockEntity(p, renderState));
                if (be != null && !this.erroredBlockEntities.contains(be)) {
                    be.setLevel(level);
                    
                    // fake cached state in case the renderer checks it as we don't want to query the actual world
                    be.setBlockState(renderState);
                    
                    pose.pushPose();
                    var bePos = r.getWorldPosition();
                    pose.translate(bePos.getX(), bePos.getY(), bePos.getZ());
                    
                    try {
                        BlockEntityRenderer<BlockEntity> renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(be);
                        if (renderer != null) {
                            renderer.render(be, ClientTicks.partialTicks, pose, buffers, GuiUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
                        }
                    } catch (Exception e) {
                        this.erroredBlockEntities.add(be);
                        BlatApi.LOGGER.error("Error rendering block entity", e);
                    }
                    pose.popPose();
                }
            }
        }
        pose.popPose();
        buffers.endBatch();
        pose.popPose();
        
    }
    
    private void renderBlock(MultiBufferSource.BufferSource buffers, ClientLevel level, BlockState state, BlockPos pos, float alpha, PoseStack ps) {
        if (pos != null) {
            ps.pushPose();
            ps.translate(pos.getX(), pos.getY(), pos.getZ());
            var blockRenderer = Minecraft.getInstance().getBlockRenderer();
            
            var fluidState = state.getFluidState();
            if (!fluidState.isEmpty()) {
                var layer = ItemBlockRenderTypes.getRenderLayer(fluidState);
                var buffer = buffers.getBuffer(layer);
                blockRenderer.renderLiquid(pos, getMultiblock(), new FluidBlockVertexConsumer(buffer, ps, pos), state, fluidState);
            }
            
            if (state.getRenderShape() != RenderShape.INVISIBLE) {
                var model = blockRenderer.getBlockModel(state);
                for (var layer : model.getRenderTypes(state, level.random, ModelData.EMPTY)) {
                    var buffer = buffers.getBuffer(layer);
                    blockRenderer.renderBatched(state, pos, getMultiblock(), ps, buffer, false, level.random, ModelData.EMPTY, layer);
                }
            }
            
            ps.popPose();
        }
    }
}
