package ru.blatfan.blatapi.utils;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import ru.blatfan.blatapi.fluffy_fur.client.render.item.CustomItemRenderer;

/**
 * use {@link GuiUtil}
 */
@UtilityClass
@Deprecated(forRemoval = true, since = "0.3.1")
public class RenderUtil {
    /**
     * use {@link GuiUtil#blitOffset}
     */
    @Deprecated(forRemoval = true, since = "0.3.1")
    public static float blitOffset = 0;
    
    /**
     * use {@link GuiUtil#getShader(RenderType)}
     */
    @Deprecated(forRemoval = true, since = "0.3.1")
    public static ShaderInstance getShader(RenderType type) {
        return GuiUtil.getShader(type);
    }
    
    /**
     * use {@link GuiUtil#getCustomItemRenderer()}
     */
    @Deprecated(forRemoval = true, since = "0.3.1")
    public static CustomItemRenderer getCustomItemRenderer() {
        return GuiUtil.getCustomItemRenderer();
    }
    
    /**
     * use {@link GuiUtil#renderItemModelInGui(ItemStack, float, float, float, float, float)}
     */
    @Deprecated(forRemoval = true, since = "0.3.1")
    public static void renderItemModelInGui(ItemStack stack, float x, float y, float xSize, float ySize, float zSize) {
        renderItemModelInGui(stack, x, y, xSize, ySize, zSize, 0, 0, 0, GuiUtil.FULL_BRIGHT);
    }
    
    /**
     * use {@link GuiUtil#renderItemModelInGui(ItemStack, float, float, float, float, float, float, float, float, int)}
     */
    @Deprecated(forRemoval = true, since = "0.3.1")
    public static void renderItemModelInGui(ItemStack stack, float x, float y, float xSize, float ySize, float zSize, float xRot, float yRot, float zRot, int packedLight) {
        Minecraft minecraft = Minecraft.getInstance();
        BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(stack, minecraft.level, minecraft.player, 0);
        CustomItemRenderer customItemRenderer = getCustomItemRenderer();
        
        PoseStack poseStack = RenderSystem.getModelViewStack();
        poseStack.pushPose();
        poseStack.translate(x, y, 100.0F + blitOffset);
        poseStack.translate((double) xSize / 2, (double) ySize / 2, 0.0D);
        poseStack.scale(1.0F, -1.0F, 1.0F);
        poseStack.scale(xSize, ySize, zSize);
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.ZP.rotationDegrees(zRot));
        RenderSystem.applyModelViewMatrix();
        PoseStack pose = new PoseStack();
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        boolean flag = !bakedmodel.usesBlockLight();
        if (flag) Lighting.setupForFlatItems();
        
        customItemRenderer.render(stack, ItemDisplayContext.GUI, false, pose, multibuffersource$buffersource, packedLight, OverlayTexture.NO_OVERLAY, bakedmodel);
        
        RenderSystem.disableDepthTest();
        multibuffersource$buffersource.endBatch();
        RenderSystem.enableDepthTest();
        if (flag) Lighting.setupFor3DItems();
        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
    }
    
    /**
     * use {@link GuiUtil#renderFloatingItemModelIntoGUI(GuiGraphics, ItemStack, float, float, int, float, float)}
     */
    @Deprecated(forRemoval = true, since = "0.3.1")
    public static void renderFloatingItemModelIntoGUI(GuiGraphics gui, ItemStack stack, float x, float y, int packedLight, float ticks, float ticksUp) {
        Minecraft minecraft = Minecraft.getInstance();
        BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(stack, minecraft.level, minecraft.player, 0);
        CustomItemRenderer customItemRenderer = getCustomItemRenderer();
        
        float old = bakedmodel.getTransforms().gui.rotation.y;
        blitOffset += 50.0F;
        
        PoseStack pose = gui.pose();
        
        pose.pushPose();
        pose.translate(x + 8, y + 8, 100 + blitOffset);
        pose.mulPoseMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
        pose.scale(16.0F, 16.0F, 16.0F);
        pose.translate(0.0D, Math.sin(Math.toRadians(ticksUp)) * 0.03125F, 0.0D);
        if (bakedmodel.usesBlockLight()) {
            bakedmodel.getTransforms().gui.rotation.y = ticks;
        } else {
            pose.mulPose(Axis.YP.rotationDegrees(ticks));
        }
        boolean flag = !bakedmodel.usesBlockLight();
        if (flag) Lighting.setupForFlatItems();
        
        customItemRenderer.renderItem(stack, ItemDisplayContext.GUI, false, pose, Minecraft.getInstance().renderBuffers().bufferSource(), packedLight, OverlayTexture.NO_OVERLAY, bakedmodel);
        
        RenderSystem.disableDepthTest();
        Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
        RenderSystem.enableDepthTest();
        if (flag) Lighting.setupFor3DItems();
        pose.popPose();
        RenderSystem.applyModelViewMatrix();
        
        bakedmodel.getTransforms().gui.rotation.y = old;
        blitOffset -= 50.0F;
    }
}