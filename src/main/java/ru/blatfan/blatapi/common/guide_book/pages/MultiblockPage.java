package ru.blatfan.blatapi.common.guide_book.pages;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.Lighting;
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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.client.render.FluidBlockVertexConsumer;
import ru.blatfan.blatapi.client.render.GhostBuffers;
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
    private final int buttonY;
    private final Color color;
    private final SplitText texts;
    
    public MultiblockPage(Component title, Color titleColor, boolean separator, ResourceLocation multiblock, int buttonY, Color color, List<Component> text) {
        super(title, titleColor, separator);
        this.multiblock = multiblock;
        this.buttonY = buttonY;
        this.color = color;
        
        if(!text.isEmpty())
            this.texts = TextPage.splitText(text, GuideClient.pageWidth-4,GuideClient.pageHeight - 20);
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
            Text c = texts.get(i);
            int ty = (int) (_buttonY + 7 + (GuideClient.font.lineHeight * i)*texts.scale());
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
        
        pose.translate(maxX/2, maxY/2, 100);
        pose.translate(-(float) sizeX / 2, -(float) sizeY / 2, 0);
        pose.scale(scale, scale, scale);
        
        pose.mulPose(Axis.XP.rotationDegrees(-30F));
        
        float offX = (float) -sizeX / 2;
        float offZ = (float) -sizeZ / 2 + 1;
        
        pose.translate(-offX, 0, -offZ);
        pose.mulPose(Axis.YP.rotationDegrees(45));
        pose.translate(offX, 0, offZ);
        
        var buffer=initBuffers(mc.renderBuffers().bufferSource());
        
        pose.pushPose();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        pose.translate(0, 0, -1);
        
        Lighting.setupForFlatItems();
        
        if(multiblockSimulation==null) multiblockSimulation = getMultiblock().simulate(level, pos, facingRotation, false, false);
        for (Multiblock.SimulateResult r : multiblockSimulation.getSecond()) {
            BlockPos renderPos = r.getWorldPosition().offset(-sizeX/2, -sizeY/2, -sizeZ/2);
            
            BlockState renderState = r.getStateMatcher().getDisplayedState(ClientTicks.ticks).rotate(facingRotation);
            this.renderBlock(buffer, renderState, renderPos, pose);
            
            if (renderState.getBlock() instanceof EntityBlock eb) {
                var be = this.blockEntityCache.computeIfAbsent(renderPos.immutable(), p -> eb.newBlockEntity(p, renderState));
                if (be != null && !this.erroredBlockEntities.contains(be)) {
                    be.setLevel(level);
                    be.setBlockState(renderState);
                    
                    pose.pushPose();
                    pose.translate(renderPos.getX(), renderPos.getY(), renderPos.getZ());
                    
                    try {
                        BlockEntityRenderer<BlockEntity> renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(be);
                        if (renderer != null)
                            renderer.render(be, ClientTicks.partialTicks, pose, buffer, GuiUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
                    } catch (Exception e) {
                        this.erroredBlockEntities.add(be);
                        BlatApi.LOGGER.error("Error rendering block entity", e);
                    }
                    pose.popPose();
                }
            }
        }
        
        Lighting.setupFor3DItems();
        
        pose.popPose();
        buffer.endBatch();
        pose.popPose();
    }
    
    private final RandomSource random = RandomSource.create();
    
    private void renderBlock(MultiBufferSource.BufferSource buffers, BlockState state, BlockPos pos, PoseStack pose) {
        if (pos == null || state.isAir()) return;
        
        pose.pushPose();
        pose.translate(pos.getX(), pos.getY(), pos.getZ());
        
        var blockRenderer = Minecraft.getInstance().getBlockRenderer();
        var fluidState = state.getFluidState();
        
        if (!fluidState.isEmpty()) {
            var layer = ItemBlockRenderTypes.getRenderLayer(fluidState);
            var buffer = buffers.getBuffer(layer);
            blockRenderer.renderLiquid(pos, getMultiblock(), new FluidBlockVertexConsumer(buffer, pose, pos), state, fluidState);
        }
        
        if (state.getRenderShape() != RenderShape.INVISIBLE) {
            var model = blockRenderer.getBlockModel(state);
            this.random.setSeed(state.getSeed(pos));
            
            for (RenderType renderType : model.getRenderTypes(state, random, ModelData.EMPTY)) {
                var buf = buffers.getBuffer(renderType);
                blockRenderer.renderBatched(state, pos, getMultiblock(), pose, buf, true, random, ModelData.EMPTY, renderType);
            }
        }
        
        pose.popPose();
    }
    
    private static MultiBufferSource.BufferSource initBuffers(MultiBufferSource.BufferSource original) {
        float alpha = 0.8f + 0.15f*(float)Math.cos(ClientTicks.ticks/20f);
        return GhostBuffers.create(original, alpha);
    }
}
