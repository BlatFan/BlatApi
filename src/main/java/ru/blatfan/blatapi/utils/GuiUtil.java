package ru.blatfan.blatapi.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import lombok.experimental.UtilityClass;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;
import org.joml.*;
import ru.blatfan.blatapi.fluffy_fur.client.render.FluffyFurRenderType;
import ru.blatfan.blatapi.fluffy_fur.client.render.RenderBuilder;
import ru.blatfan.blatapi.fluffy_fur.client.render.item.CustomItemRenderer;
import ru.blatfan.blatapi.fluffy_fur.registry.client.FluffyFurRenderTypes;
import ru.blatfan.blatapi.utils.collection.SplitText;

import java.awt.*;
import java.lang.Math;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.minecraft.util.Mth.sqrt;

@UtilityClass@SuppressWarnings("ALL")
public class GuiUtil {
    public static GameProfile createOfflineProfile(String playerName) {
        UUID offlineUUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes());
        return new GameProfile(offlineUUID, playerName);
    }
    public static void renderFlatPlayerHead(GuiGraphics guiGraphics, int x, int y, int size, String player) {
        renderFlatPlayerHead(guiGraphics, x, y, size, createOfflineProfile(player));
    }
    public static void renderFlatPlayerHead(GuiGraphics guiGraphics, int x, int y, int size, GameProfile player) {
        ResourceLocation skinTexture = Minecraft.getInstance().getSkinManager().getInsecureSkinLocation(player);
        
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        
        guiGraphics.blit(skinTexture, x, y, size, size, 8f, 8f, 8, 8, 64, 64);
        guiGraphics.blit(skinTexture, x, y, size, size, 40f, 8f, 8, 8, 64, 64);
        
        RenderSystem.disableBlend();
    }
    public static void renderFlatPlayerHead(GuiGraphics guiGraphics, int x, int y, int size, Player player) {
        ResourceLocation skinTexture = Minecraft.getInstance().getSkinManager().getInsecureSkinLocation(player.getGameProfile());
        
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        
        guiGraphics.blit(skinTexture, x, y, size, size, 8f, 8f, 8, 8, 64, 64);
        guiGraphics.blit(skinTexture, x, y, size, size, 40f, 8f, 8, 8, 64, 64);
        
        RenderSystem.disableBlend();
    }
    
    public static void render3DPlayerHeadMouse(GuiGraphics guiGraphics, int x, int y, int size, float mouseX, float mouseY, Player player) {
        render3DPlayerHead(guiGraphics, x, y, size, (mouseX - x) * 0.1f, (mouseY - y) * 0.25f, player);
    }
    
    public static void render3DPlayerHead(GuiGraphics guiGraphics, int x, int y, int size, float degreeX, float degreeY, Player player) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        
        poseStack.translate(x, y, 100);
        poseStack.scale(size, size, size);
        
        poseStack.mulPose(Axis.YP.rotationDegrees(degreeX));
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        poseStack.mulPose(Axis.XP.rotationDegrees(-degreeY));
        
        renderPlayerHeadCube(player, poseStack);
        
        poseStack.popPose();
    }
    
    private static void renderPlayerHeadCube(Player player, PoseStack poseStack) {
        ResourceLocation skinTexture = Minecraft.getInstance().getSkinManager().getInsecureSkinLocation(player.getGameProfile());
        
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderType renderType = RenderType.entityCutoutNoCull(skinTexture);
        VertexConsumer buffer = bufferSource.getBuffer(renderType);
        
        float s = 4f;
        int light = 15728880;
        Matrix4f m = poseStack.last().pose();
        Matrix3f n = poseStack.last().normal();
        int overlay = LivingEntityRenderer.getOverlayCoords(player, 0f);
        float u1 = 8f/64f, v1 = 8f/64f, u2 = 16f/64f, v2 = 16f/64f;
        buffer.vertex(m, -s,  s, -s).color(255,255,255,255).uv(u1,v1).overlayCoords(overlay).uv2(light).normal(n, 0,0,-1).endVertex();
        buffer.vertex(m,  s,  s, -s).color(255,255,255,255).uv(u2,v1).overlayCoords(overlay).uv2(light).normal(n, 0,0,-1).endVertex();
        buffer.vertex(m,  s, -s, -s).color(255,255,255,255).uv(u2,v2).overlayCoords(overlay).uv2(light).normal(n, 0,0,-1).endVertex();
        buffer.vertex(m, -s, -s, -s).color(255,255,255,255).uv(u1,v2).overlayCoords(overlay).uv2(light).normal(n, 0,0,-1).endVertex();
        
        u1 = 24f/64f; v1 = 8f/64f; u2 = 32f/64f; v2 = 16f/64f;
        buffer.vertex(m,  s,  s,  s).color(255,255,255,255).uv(u1,v1).overlayCoords(overlay).uv2(light).normal(n, 0,0,1).endVertex();
        buffer.vertex(m, -s,  s,  s).color(255,255,255,255).uv(u2,v1).overlayCoords(overlay).uv2(light).normal(n, 0,0,1).endVertex();
        buffer.vertex(m, -s, -s,  s).color(255,255,255,255).uv(u2,v2).overlayCoords(overlay).uv2(light).normal(n, 0,0,1).endVertex();
        buffer.vertex(m,  s, -s,  s).color(255,255,255,255).uv(u1,v2).overlayCoords(overlay).uv2(light).normal(n, 0,0,1).endVertex();
        
        u1 = 0f/64f; v1 = 8f/64f; u2 = 8f/64f; v2 = 16f/64f;
        buffer.vertex(m, -s,  s,  s).color(255,255,255,255).uv(u1,v1).overlayCoords(overlay).uv2(light).normal(n, -1,0,0).endVertex();
        buffer.vertex(m, -s,  s, -s).color(255,255,255,255).uv(u2,v1).overlayCoords(overlay).uv2(light).normal(n, -1,0,0).endVertex();
        buffer.vertex(m, -s, -s, -s).color(255,255,255,255).uv(u2,v2).overlayCoords(overlay).uv2(light).normal(n, -1,0,0).endVertex();
        buffer.vertex(m, -s, -s,  s).color(255,255,255,255).uv(u1,v2).overlayCoords(overlay).uv2(light).normal(n, -1,0,0).endVertex();
        
        u1 = 16f/64f; v1 = 8f/64f; u2 = 24f/64f; v2 = 16f/64f;
        buffer.vertex(m,  s,  s, -s).color(255,255,255,255).uv(u1,v1).overlayCoords(overlay).uv2(light).normal(n, 1,0,0).endVertex();
        buffer.vertex(m,  s,  s,  s).color(255,255,255,255).uv(u2,v1).overlayCoords(overlay).uv2(light).normal(n, 1,0,0).endVertex();
        buffer.vertex(m,  s, -s,  s).color(255,255,255,255).uv(u2,v2).overlayCoords(overlay).uv2(light).normal(n, 1,0,0).endVertex();
        buffer.vertex(m,  s, -s, -s).color(255,255,255,255).uv(u1,v2).overlayCoords(overlay).uv2(light).normal(n, 1,0,0).endVertex();
        
        u1 = 16f/64f; v1 = 0f/64f; u2 = 24f/64f; v2 = 8f/64f;
        buffer.vertex(m, -s, -s, -s).color(255,255,255,255).uv(u1,v1).overlayCoords(overlay).uv2(light).normal(n, 0,-1,0).endVertex();
        buffer.vertex(m,  s, -s, -s).color(255,255,255,255).uv(u2,v1).overlayCoords(overlay).uv2(light).normal(n, 0,-1,0).endVertex();
        buffer.vertex(m,  s, -s,  s).color(255,255,255,255).uv(u2,v2).overlayCoords(overlay).uv2(light).normal(n, 0,-1,0).endVertex();
        buffer.vertex(m, -s, -s,  s).color(255,255,255,255).uv(u1,v2).overlayCoords(overlay).uv2(light).normal(n, 0,-1,0).endVertex();
        
        u1 = 8f/64f; v1 = 0f/64f; u2 = 16f/64f; v2 = 8f/64f;
        buffer.vertex(m, -s,  s,  s).color(255,255,255,255).uv(u1,v1).overlayCoords(overlay).uv2(light).normal(n, 0,1,0).endVertex();
        buffer.vertex(m,  s,  s,  s).color(255,255,255,255).uv(u2,v1).overlayCoords(overlay).uv2(light).normal(n, 0,1,0).endVertex();
        buffer.vertex(m,  s,  s, -s).color(255,255,255,255).uv(u2,v2).overlayCoords(overlay).uv2(light).normal(n, 0,1,0).endVertex();
        buffer.vertex(m, -s,  s, -s).color(255,255,255,255).uv(u1,v2).overlayCoords(overlay).uv2(light).normal(n, 0,1,0).endVertex();
        
        bufferSource.endBatch();
    }
    
    public static void renderEntityFollowsMouse(GuiGraphics gui, int x, int y, int scale, float mouseX, float mouseY, LivingEntity entity){
        float f = (float)Math.atan(mouseX / 40.0F);
        float f1 = (float)Math.atan(mouseY / 40.0F);
        renderEntityFollowsAngle(gui, x, y, scale, f, f1, entity);
    }
    public static void renderEntityFollowsAngle(GuiGraphics gui, int x, int y, int scale, float entityLookX, float entityLookY, LivingEntity entity){
        Quaternionf quaternionf = (new Quaternionf()).rotateZ((float)Math.PI);
        Quaternionf quaternionf1 = (new Quaternionf()).rotateX(entityLookY * 20.0F * ((float)Math.PI / 180F));
        quaternionf.mul(quaternionf1);
        float f2 = entity.yBodyRot;
        float f3 = entity.getYRot();
        float f4 = entity.getXRot();
        float f5 = entity.yHeadRotO;
        float f6 = entity.yHeadRot;
        entity.yBodyRot = 180.0F + entityLookX * 20.0F;
        entity.setYRot(180.0F + entityLookX * 40.0F);
        entity.setXRot(-entityLookY * 20.0F);
        entity.yHeadRot = entity.getYRot();
        entity.yHeadRotO = entity.getYRot();
        renderEntity(gui, x, y, scale, quaternionf, quaternionf1, entity);
        entity.yBodyRot = f2;
        entity.setYRot(f3);
        entity.setXRot(f4);
        entity.yHeadRotO = f5;
        entity.yHeadRot = f6;
    }
    public static void renderEntityQuaternionf(GuiGraphics gui, int x, int y, int scale, Quaternionf quaternionf, Quaternionf quaternionf1, LivingEntity entity){
        quaternionf.mul(quaternionf1);
        renderEntity(gui, x, y, scale, quaternionf, quaternionf1, entity);
    }
    public static void renderEntity(GuiGraphics gui, int x, int y, int scale, Quaternionf pose, Quaternionf cameraOrientation, LivingEntity entity) {
        gui.pose().pushPose();
        gui.pose().translate(x, y, 50.0D);
        gui.pose().mulPoseMatrix((new Matrix4f()).scaling((float)scale, (float)scale, (float)(-scale)));
        gui.pose().mulPose(pose);
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        if (cameraOrientation != null) {
            cameraOrientation.conjugate();
            entityrenderdispatcher.overrideCameraOrientation(cameraOrientation);
        }
        entityrenderdispatcher.setRenderShadow(false);
        entityrenderdispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, gui.pose(), gui.bufferSource(), 15728880);
        gui.flush();
        entityrenderdispatcher.setRenderShadow(true);
        gui.pose().popPose();
        Lighting.setupFor3DItems();
    }
    
    public static void renderScaledIngredient(GuiGraphics gui, int x, int y, Ingredient ingredient, float scale){
        ItemStack[] stacks = ingredient.getItems();
        if(stacks.length==0) return;
        int index = (int) (System.currentTimeMillis() / 1000 % stacks.length);
        renderScaledItem(gui, stacks[index], x, y, scale);
        renderScaledItemDecorations(gui, stacks[index], x, y, scale);
    }
    public static void renderIngredient(GuiGraphics gui, int x, int y, Ingredient ingredient){
        renderScaledIngredient(gui, x,y, ingredient, 1);
    }
    public static void renderTooltip(GuiGraphics gui, int x, int y, Ingredient ingredient, float scale){
        ItemStack[] stacks = ingredient.getItems();
        if(stacks.length==0) return;
        int index = (int) (System.currentTimeMillis() / 1000 % stacks.length);
        List<Component> components = new ArrayList<>(Screen.getTooltipFromItem(Minecraft.getInstance(), stacks[index]));
        if(ingredient.values[0] instanceof Ingredient.TagValue tagValue && Minecraft.getInstance().options.advancedItemTooltips)
            components.add(1, Component.literal("Tag: #"+ tagValue.tag.location()).withStyle(ChatFormatting.DARK_GRAY));
        renderScaledTooltip(gui, components, x, y, scale);
    }
    public static void renderTooltip(GuiGraphics gui, int x, int y, Ingredient ingredient){
        renderTooltip(gui, x,y, ingredient, 1);
    }
    
    public static void drawScaledString(GuiGraphics gui, String text, int x, int y, Color color, float size){
        gui.pose().pushPose();
        gui.pose().scale(size, size, 1);
        gui.drawString(Minecraft.getInstance().font, text, x / size, y / size, ColorHelper.getColor(color), true);
        gui.pose().popPose();
    }
    public static void drawScaledString(GuiGraphics gui, Component text, int x, int y, Color color, float size){
        drawScaledString(gui, text.getString(), x, y, color, size);
    }
    
    public static void drawScaledCentreString(GuiGraphics gui, String text, int x, int y, Color color, float size){
        drawScaledString(gui, text, x-Minecraft.getInstance().font.width(text)/2, y, color, size);
    }
    public static void drawScaledCentreString(GuiGraphics gui, Component text, int x, int y, Color color, float size){
        drawScaledCentreString(gui, text.getString(), x, y, color, size);
    }
    
    public static void drawScaledTooltips(GuiGraphics gui, Component text, int x, int y, float size){
        gui.pose().pushPose();
        gui.pose().scale(size, size, 1);
        gui.renderTooltip(Minecraft.getInstance().font, text, (int) (x / size), (int) (y / size));
        gui.pose().popPose();
    }
    public static void drawScaledTooltips(GuiGraphics gui, List<Component> text, int x, int y, float size){
        gui.pose().pushPose();
        gui.pose().scale(size, size, 1);
        gui.renderComponentTooltip(Minecraft.getInstance().font, text, (int) (x / size), (int) (y / size));
        gui.pose().popPose();
    }
    
    public static void renderScaledTooltip(GuiGraphics gui, List<Component> list, int x, int y, float size) {
        gui.pose().pushPose();
        gui.pose().scale(size, size, 1);
        gui.renderComponentTooltip(Minecraft.getInstance().font, list, (int) (x/size), (int) (y/size));
        gui.pose().popPose();
    }
    public static void renderScaledItem(GuiGraphics gui, ItemStack pStack, int x, int y, float size) {
        gui.pose().pushPose();
        gui.pose().scale(size, size, 1);
        gui.renderItem(pStack, (int) (x/size), (int) (y/size));
        gui.pose().popPose();
    }
    public static void renderScaledItemTooltip(GuiGraphics gui, ItemStack pStack, int x, int y, float size) {
        gui.pose().pushPose();
        gui.pose().scale(size, size, 1);
        gui.renderTooltip(Minecraft.getInstance().font, pStack, (int) (x/size), (int) (y/size));
        gui.pose().popPose();
    }
    public static void renderScaledFakeItem(GuiGraphics gui, ItemStack pStack, int x, int y, float size) {
        gui.pose().pushPose();
        gui.pose().scale(size, size, 1);
        gui.renderFakeItem(pStack, (int) (x/size), (int) (y/size));
        gui.pose().popPose();
    }
    public static void renderScaledItemDecorations(GuiGraphics gui, ItemStack pStack, int x, int y, float size) {
        gui.pose().pushPose();
        gui.pose().scale(size, size, 1);
        gui.renderItemDecorations(Minecraft.getInstance().font, pStack, (int) (x/size), (int) (y/size));
        gui.pose().popPose();
    }
    
    public static void blit(GuiGraphics gui, float scale, int x, int y, int pBlitOffset, int pWidth, int pHeight, TextureAtlasSprite pSprite) {
        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.scale(scale, scale, 1);
        gui.blit((int) (x/scale), (int) (y/scale), pBlitOffset, pWidth, pHeight, pSprite);
        pose.popPose();
    }
    
    public static void blit(GuiGraphics gui, float scale, int x, int y, int pBlitOffset, int pWidth, int pHeight, TextureAtlasSprite pSprite, float pRed, float pGreen, float pBlue, float pAlpha) {
        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.scale(scale, scale, 1);
        gui.blit((int) (x/scale), (int) (y/scale), pBlitOffset, pWidth, pHeight, pSprite, pRed, pGreen, pBlue, pAlpha);
        pose.popPose();
    }
    
    public static void blit(GuiGraphics gui, float scale, ResourceLocation pAtlasLocation, int x, int y, int pUOffset, int pVOffset, int pUWidth, int pVHeight) {
        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.scale(scale, scale, 1);
        gui.blit(pAtlasLocation, (int) (x/scale), (int) (y/scale), pUOffset, pVOffset, pUWidth, pVHeight);
        pose.popPose();
    }
    
    public static void blit(GuiGraphics gui, float scale, ResourceLocation pAtlasLocation, int x, int y, int pBlitOffset, float pUOffset, float pVOffset, int pUWidth, int pVHeight, int pTextureWidth, int pTextureHeight) {
        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.scale(scale, scale, 1);
        gui.blit(pAtlasLocation, (int) (x/scale), (int) (y/scale), pBlitOffset, pUOffset, pVOffset, pUWidth, pVHeight, pTextureWidth, pTextureHeight);
        pose.popPose();
    }
    
    public static void blit(GuiGraphics gui, float scale, ResourceLocation pAtlasLocation, int x, int y, int pWidth, int pHeight, float pUOffset, float pVOffset, int pUWidth, int pVHeight, int pTextureWidth, int pTextureHeight) {
        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.scale(scale, scale, 1);
        gui.blit(pAtlasLocation, (int) (x/scale), (int) (y/scale), pWidth, pHeight, pUOffset, pVOffset, pUWidth, pVHeight, pTextureWidth, pTextureHeight);
        pose.popPose();
    }
    
    public static void blit(GuiGraphics gui, float scale, ResourceLocation pAtlasLocation, int x, int y, float pUOffset, float pVOffset, int pWidth, int pHeight, int pTextureWidth, int pTextureHeight) {
        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.scale(scale, scale, 1);
        gui.blit(pAtlasLocation, (int) (x/scale), (int) (y/scale), pUOffset, pVOffset, pWidth, pHeight, pTextureWidth, pTextureHeight);
        pose.popPose();
    }
    
    public static void blitNineSlicedSized(GuiGraphics gui, float scale, ResourceLocation texture, int x, int y, int width, int height, int sliceSize, int uWidth, int vHeight, int uOffset, int vOffset,
                                     int textureWidth, int textureHeight) {
        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.scale(scale, scale, 1);
        gui.blitNineSlicedSized(texture, (int) (x/scale), (int) (y/scale), width, height, sliceSize, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight);
        pose.popPose();
    }
    
    public static void blitNineSlicedSized(GuiGraphics gui, float scale, ResourceLocation texture, int x, int y, int width, int height, int sliceWidth, int sliceHeight, int uWidth, int vHeight,
                                     int uOffset, int vOffset, int textureWidth, int textureHeight) {
        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.scale(scale, scale, 1);
        gui.blitNineSlicedSized(texture, (int) (x/scale), (int) (y/scale), width, height, sliceWidth, sliceHeight, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight);
        pose.popPose();
    }
    
    public static void blitNineSlicedSized(GuiGraphics gui, float scale, ResourceLocation texture, int x, int y, int width, int height, int cornerWidth, int cornerHeight, int edgeWidth, int edgeHeight,
                                     int uWidth, int vHeight, int uOffset, int vOffset, int textureWidth, int textureHeight) {
        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.scale(scale, scale, 1);
        gui.blitNineSlicedSized(texture, (int) (x/scale), (int) (y/scale), width, height, cornerWidth, cornerHeight, edgeWidth, edgeHeight, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight);
        pose.popPose();
    }
    
    public static void drawGradientRect(Matrix4f mat, int zLevel, int left, int top, int right, int bottom, int startColor, int endColor) {
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        drawGradientRect(mat, bufferBuilder, left, top, right, bottom, zLevel, startColor, endColor);
        BufferUploader.drawWithShader(bufferBuilder.end());
        
        RenderSystem.disableBlend();
    }
    
    public static void drawGradientRect(Matrix4f mat, BufferBuilder bufferBuilder, int left, int top, int right, int bottom, int zLevel, int startColor, int endColor) {
        float startAlpha = (float)(startColor >> 24 & 255) / 255.0F;
        float startRed   = (float)(startColor >> 16 & 255) / 255.0F;
        float startGreen = (float)(startColor >>  8 & 255) / 255.0F;
        float startBlue  = (float)(startColor       & 255) / 255.0F;
        float endAlpha   = (float)(endColor   >> 24 & 255) / 255.0F;
        float endRed     = (float)(endColor   >> 16 & 255) / 255.0F;
        float endGreen   = (float)(endColor   >>  8 & 255) / 255.0F;
        float endBlue    = (float)(endColor         & 255) / 255.0F;
        
        bufferBuilder.vertex(mat, right,    top, zLevel).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        bufferBuilder.vertex(mat,  left,    top, zLevel).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        bufferBuilder.vertex(mat,  left, bottom, zLevel).color(  endRed,   endGreen,   endBlue,   endAlpha).endVertex();
        bufferBuilder.vertex(mat, right, bottom, zLevel).color(  endRed,   endGreen,   endBlue,   endAlpha).endVertex();
    }
    
    public static void drawGradientRectHorizontal(Matrix4f mat, int zLevel, int left, int top, int right, int bottom, int startColor, int endColor) {
        float startAlpha = (float)(startColor >> 24 & 255) / 255.0F;
        float startRed   = (float)(startColor >> 16 & 255) / 255.0F;
        float startGreen = (float)(startColor >>  8 & 255) / 255.0F;
        float startBlue  = (float)(startColor       & 255) / 255.0F;
        float endAlpha   = (float)(endColor   >> 24 & 255) / 255.0F;
        float endRed     = (float)(endColor   >> 16 & 255) / 255.0F;
        float endGreen   = (float)(endColor   >>  8 & 255) / 255.0F;
        float endBlue    = (float)(endColor         & 255) / 255.0F;
        
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(mat, right,    top, zLevel).color(  endRed,   endGreen,   endBlue,   endAlpha).endVertex();
        buffer.vertex(mat,  left,    top, zLevel).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        buffer.vertex(mat,  left, bottom, zLevel).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        buffer.vertex(mat, right, bottom, zLevel).color(  endRed,   endGreen,   endBlue,   endAlpha).endVertex();
        tessellator.end();
        
        RenderSystem.disableBlend();
    }
    
    public static void blit(PoseStack poseStack, int x, int y, int width, int height, float texX, float texY, int texWidth, int texHeight, int fullWidth, int fullHeight) {
        blit(poseStack, x, x + width, y, y + height, 0, texWidth, texHeight, texX, texY, fullWidth, fullHeight);
    }
    public static void blit(PoseStack poseStack, int x0, int x1, int y0, int y1, int z, int texWidth, int texHeight, float texX, float texY, int fullWidth, int fullHeight) {
        innerBlit(poseStack, x0, x1, y0, y1, z, (texX + 0.0F) / (float)fullWidth, (texX + (float)texWidth) / (float)fullWidth, (texY + 0.0F) / (float)fullHeight, (texY + (float)texHeight) / (float)fullHeight);
    }
    
    private static void innerBlit(PoseStack poseStack, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f matrix4f = poseStack.last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f, (float)x0, (float)y0, (float)z).uv(u0, v0).endVertex();
        bufferbuilder.vertex(matrix4f, (float)x0, (float)y1, (float)z).uv(u0, v1).endVertex();
        bufferbuilder.vertex(matrix4f, (float)x1, (float)y1, (float)z).uv(u1, v1).endVertex();
        bufferbuilder.vertex(matrix4f, (float)x1, (float)y0, (float)z).uv(u1, v0).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }
    /**
     * drawString for rendering at float coordinates.
     */
    public static int drawString(GuiGraphics guiGraphics, Font font, @Nullable Component component, float x, float y, int color, boolean drawShadow) {
        if (component == null) {
            return 0;
        } else {
            int i = font.drawInBatch(component, x, y, color, drawShadow, guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
            guiGraphics.flushIfUnmanaged();
            return i;
        }
    }
    
    /**
     * drawString for rendering at float coordinates.
     */
    public static int drawString(GuiGraphics guiGraphics, Font font, @Nullable String string, float x, float y, int color, boolean drawShadow) {
        if (string == null) {
            return 0;
        } else {
            int i = font.drawInBatch(string, x, y, color, drawShadow, guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
            guiGraphics.flushIfUnmanaged();
            return i;
        }
    }
    
    /**
     * drawString for rendering at float coordinates.
     */
    public static int drawString(GuiGraphics guiGraphics, Font font, @Nullable FormattedCharSequence string, float x, float y, int color, boolean drawShadow) {
        if (string == null) {
            return 0;
        } else {
            int i = font.drawInBatch(string, x, y, color, drawShadow, guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
            guiGraphics.flushIfUnmanaged();
            return i;
        }
    }
    
    private static CustomItemRenderer customItemRenderer;
    public static float blitOffset = 0;
    
    public static int FULL_BRIGHT = 15728880;
    
    public static Function<Float, Float> FULL_WIDTH_FUNCTION = (f) -> 1f;
    public static Function<Float, Float> LINEAR_IN_WIDTH_FUNCTION = (f) -> f;
    public static Function<Float, Float> LINEAR_OUT_WIDTH_FUNCTION = (f) -> 1f - f;
    public static Function<Float, Float> LINEAR_IN_ROUND_WIDTH_FUNCTION = (f) -> f == 1 ? 0 : f;
    public static Function<Float, Float> LINEAR_OUT_ROUND_WIDTH_FUNCTION = (f) -> f == 0 ? 0 : 1f - f;
    public static Function<Float, Float> LINEAR_IN_SEMI_ROUND_WIDTH_FUNCTION = (f) -> f == 1 ? 0.5f : f;
    public static Function<Float, Float> LINEAR_OUT_SEMI_ROUND_WIDTH_FUNCTION = (f) -> f == 0 ? 0.5f : 1f - f;
    
    public static ShaderInstance getShader(RenderType type) {
        if (type instanceof FluffyFurRenderType renderType) {
            Optional<Supplier<ShaderInstance>> shader = renderType.state.shaderState.shader;
            if (shader.isPresent()) {
                return shader.get().get();
            }
        }
        return null;
    }
    
    public static CustomItemRenderer getCustomItemRenderer() {
        Minecraft minecraft = Minecraft.getInstance();
        if (customItemRenderer == null) customItemRenderer = new CustomItemRenderer(minecraft, minecraft.getTextureManager(), minecraft.getModelManager(), minecraft.getItemColors(), minecraft.getItemRenderer().getBlockEntityRenderer());
        return customItemRenderer;
    }
    
    public static void renderItemModelInGui(ItemStack stack, float x, float y, float xSize, float ySize, float zSize) {
        renderItemModelInGui(stack, x, y, xSize, ySize, zSize, 0, 0, 0, FULL_BRIGHT);
    }
    
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
    
    public static void renderArmorInGui(GuiGraphics gui, ItemStack stack, int x, int y, int z, float sizeX, float sizeY, float sizeZ,
                                        float xRot, float yRot, float zRot, int packedLight) {
        if(!(stack.getItem() instanceof ArmorItem armorItem)) return;
        Minecraft mc = Minecraft.getInstance();
        EquipmentSlot slot = LivingEntity.getEquipmentSlotForItem(stack);
        
        EntityRendererProvider.Context context = new EntityRendererProvider.Context(mc.getEntityRenderDispatcher(), mc.getItemRenderer(),
            mc.getBlockRenderer(), mc.getEntityRenderDispatcher().getItemInHandRenderer(), mc.getResourceManager(),
            mc.getEntityModels(), mc.font);
        ModelPart playerModel = slot==EquipmentSlot.LEGS ?
            context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR) : context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR);
        Model model = ForgeHooksClient.getArmorModel(mc.player, stack, slot, new HumanoidArmorModel<>(playerModel));
        if(model instanceof HumanoidModel<?> humanoidModel){
            humanoidModel.head.visible = slot==EquipmentSlot.HEAD;
            humanoidModel.hat.visible = slot==EquipmentSlot.HEAD;
            humanoidModel.body.visible = slot==EquipmentSlot.CHEST;
            humanoidModel.leftArm.visible = slot==EquipmentSlot.CHEST;
            humanoidModel.rightArm.visible = slot==EquipmentSlot.CHEST;
            humanoidModel.rightLeg.visible = slot==EquipmentSlot.LEGS || slot==EquipmentSlot.FEET;
            humanoidModel.leftLeg.visible = slot==EquipmentSlot.LEGS || slot==EquipmentSlot.FEET;
            humanoidModel.young=false;
        }
        
        int yOffset = (int) (-sizeY+(sizeY/16)*switch(slot){
            case HEAD -> 4;
            case CHEST -> 14;
            case LEGS -> 24;
            case FEET -> 28;
            default -> 0;
        });
        
        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.translate(x+(sizeX/2), y-yOffset, z+blitOffset);
        pose.scale(sizeX, sizeY, sizeZ);
        pose.mulPose(Axis.XP.rotationDegrees(xRot));
        pose.mulPose(Axis.YP.rotationDegrees(yRot));
        pose.mulPose(Axis.ZP.rotationDegrees(zRot));
        
        RenderSystem.enableDepthTest();
        Lighting.setupForFlatItems();
        
        MultiBufferSource.BufferSource bufferSource = gui.bufferSource();
        
        if (armorItem instanceof DyeableLeatherItem) {
            Color color = new Color(((DyeableLeatherItem)armorItem).getColor(stack));
            renderModel(pose, bufferSource, packedLight, model,
                color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1f,
                RenderType.armorCutoutNoCull(getArmorResource(mc.player, stack, slot, null)));
            renderModel(pose, bufferSource, packedLight, model, 1f, 1f, 1f, 1f,
                RenderType.armorCutoutNoCull(getArmorResource(mc.player, stack, slot, "overlay")));
        } else {
            renderModel(pose, bufferSource, packedLight, model, 1f, 1f, 1f, 1f,
                RenderType.armorCutoutNoCull(getArmorResource(mc.player, stack, slot, null)));
        }
        
        ArmorTrim.getTrim(mc.player.level().registryAccess(), stack).ifPresent((trim) ->
            renderTrim(armorItem.getMaterial(), pose, bufferSource, packedLight, trim, model));
        if(stack.hasFoil())
            renderGlint(pose, bufferSource, RenderType.armorEntityGlint(), packedLight, model, 1, 1, 1, 1);
        
        bufferSource.endBatch();
        Lighting.setupFor3DItems();
        RenderSystem.disableDepthTest();
        
        pose.popPose();
    }
    
    public static void renderGlint(PoseStack poseStack, MultiBufferSource buffer, RenderType renderType, int packedLight, Model model,
                                   float red, float green, float blue, float alpha) {
        if(red<0 || red>1) throw new IllegalArgumentException("0<=red<=1, red="+red);
        if(green<0 || green>1) throw new IllegalArgumentException("0<=green<=1, green="+green);
        if(blue<0 || blue>1) throw new IllegalArgumentException("0<=blue<=1, blue="+blue);
        if(alpha<0 || alpha>1) throw new IllegalArgumentException("0<=alpha<=1, alpha="+alpha);
        model.renderToBuffer(poseStack, buffer.getBuffer(renderType), packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
    }
    
    public static ResourceLocation getArmorResource(Entity entity, ItemStack stack, EquipmentSlot slot, @javax.annotation.Nullable String type) {
        String s1 = getDefaultArmorPath(stack, slot, type);
        s1 = ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
        ResourceLocation resourcelocation = HumanoidArmorLayer.ARMOR_LOCATION_CACHE.get(s1);
        if (resourcelocation == null) resourcelocation = new ResourceLocation(s1);
        return resourcelocation;
    }
    
    public static String getDefaultArmorPath(ItemStack stack, EquipmentSlot slot, String type) {
        ArmorItem item = (ArmorItem) stack.getItem();
        String texture = item.getMaterial().getName();
        String namespace = "minecraft";
        int idx = texture.indexOf(':');
        if (idx != -1) {
            namespace = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }
        int layer = slot.isArmor() && slot.equals(EquipmentSlot.LEGS) ? 2 : 1;
        return String.format(Locale.ROOT, "%s:textures/models/armor/%s_layer_%d%s.png",
            namespace, texture, layer, type == null ? "" : String.format(Locale.ROOT, "_%s", type));
    }
    
    public static void renderTrim(ArmorMaterial armorMaterial, PoseStack poseStack, MultiBufferSource buffer, int packedLight, ArmorTrim trim, Model model) {
        TextureAtlas armorTrimAtlas = Minecraft.getInstance().getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET);
        TextureAtlasSprite textureatlassprite = armorTrimAtlas.getSprite(trim.outerTexture(armorMaterial));
        VertexConsumer vertexconsumer = textureatlassprite.wrap(buffer.getBuffer(Sheets.armorTrimsSheet()));
        model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
    }
    
    public static void renderCustomModel(ModelResourceLocation model, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getModelManager().getModel(model);
        Minecraft.getInstance().getItemRenderer().render(new ItemStack(Items.DIRT), displayContext, leftHand, poseStack, buffer, combinedLight, combinedOverlay, bakedmodel);
    }
    
    public static void renderBlockModel(ModelResourceLocation model, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        BakedModel bakedmodel = Minecraft.getInstance().getModelManager().getModel(model);
        Minecraft.getInstance().getItemRenderer().render(new ItemStack(Items.DIRT), displayContext, leftHand, poseStack, buffer, combinedLight, combinedOverlay, bakedmodel);
    }
    
    public static TextureAtlasSprite getSprite(ResourceLocation resourceLocation) {
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(resourceLocation);
    }
    
    public static TextureAtlasSprite getSprite(String modId, String sprite) {
        return getSprite(new ResourceLocation(modId, sprite));
    }
    
    public static void renderFluid(PoseStack stack, FluidStack fluidStack, float size, float texSize, boolean flowing, int light) {
        renderFluid(stack, fluidStack, size, size, size, texSize, texSize, texSize, flowing, light);
    }
    
    public static void renderFluid(PoseStack stack, FluidStack fluidStack, float size, float texSize, Color color, boolean flowing, int light) {
        renderFluid(stack, fluidStack, size, size, size, texSize, texSize, texSize, color, flowing, light);
    }
    
    public static void renderFluid(PoseStack stack, FluidStack fluidStack, float width, float height, float length, float texWidth, float texHeight, float texLength, boolean flowing, int light) {
        if (!fluidStack.isEmpty()) {
            RenderBuilder builder = getFluidRenderBuilder(fluidStack, texWidth, texHeight, texLength, flowing, light);
            builder.renderCube(stack, width, height, length);
        }
    }
    
    public static void renderFluid(PoseStack stack, FluidStack fluidStack, float width, float height, float length, float texWidth, float texHeight, float texLength, Color color, boolean flowing, int light) {
        if (!fluidStack.isEmpty()) {
            RenderBuilder builder = getFluidRenderBuilder(fluidStack, texWidth, texHeight, texLength, flowing, light);
            builder.setColor(color).renderCube(stack, width, height, length);
        }
    }
    
    public static void renderCenteredFluid(PoseStack stack, FluidStack fluidStack, float size, float texSize, boolean flowing, int light) {
        renderCenteredFluid(stack, fluidStack, size, size, size, texSize, texSize, texSize, flowing, light);
    }
    
    public static void renderCenteredFluid(PoseStack stack, FluidStack fluidStack, float size, float texSize, Color color, boolean flowing, int light) {
        renderCenteredFluid(stack, fluidStack, size, size, size, texSize, texSize, texSize, color, flowing, light);
    }
    
    public static void renderCenteredFluid(PoseStack stack, FluidStack fluidStack, float width, float height, float length, float texWidth, float texHeight, float texLength, boolean flowing, int light) {
        if (!fluidStack.isEmpty()) {
            RenderBuilder builder = getFluidRenderBuilder(fluidStack, texWidth, texHeight, texLength, flowing, light);
            builder.renderCenteredCube(stack, width, height, length);
        }
    }
    
    public static void renderCenteredFluid(PoseStack stack, FluidStack fluidStack, float width, float height, float length, float texWidth, float texHeight, float texLength, Color color, boolean flowing, int light) {
        if (!fluidStack.isEmpty()) {
            RenderBuilder builder = getFluidRenderBuilder(fluidStack, texWidth, texHeight, texLength, flowing, light);
            builder.setColor(color).renderCenteredCube(stack, width, height, length);
        }
    }
    
    public static void renderWavyFluid(PoseStack stack, FluidStack fluidStack, float size, float texSize, boolean flowing, int light, float strength, float time) {
        renderWavyFluid(stack, fluidStack, size, size, size, texSize, texSize, texSize, flowing, light, strength, time);
    }
    
    public static void renderWavyFluid(PoseStack stack, FluidStack fluidStack, float size, float texSize, Color color, boolean flowing, int light, float strength, float time) {
        renderWavyFluid(stack, fluidStack, size, size, size, texSize, texSize, texSize, color, flowing, light, strength, time);
    }
    
    public static void renderWavyFluid(PoseStack stack, FluidStack fluidStack, float width, float height, float length, float texWidth, float texHeight, float texLength, boolean flowing, int light, float strength, float time) {
        if (!fluidStack.isEmpty()) {
            RenderBuilder builder = getFluidRenderBuilder(fluidStack, texWidth, texHeight, texLength, flowing, light);
            builder.renderWavyCube(stack, width, height, length, strength, time);
        }
    }
    
    public static void renderWavyFluid(PoseStack stack, FluidStack fluidStack, float width, float height, float length, float texWidth, float texHeight, float texLength, Color color, boolean flowing, int light, float strength, float time) {
        if (!fluidStack.isEmpty()) {
            RenderBuilder builder = getFluidRenderBuilder(fluidStack, texWidth, texHeight, texLength, flowing, light);
            builder.setColor(color).renderWavyCube(stack, width, height, length, strength, time);
        }
    }
    
    public static RenderBuilder getFluidRenderBuilder(FluidStack fluidStack, float texWidth, float texHeight, float texLength, boolean flowing, int light) {
        RenderBuilder builder = RenderBuilder.create().setRenderType(FluffyFurRenderTypes.TRANSLUCENT_TEXTURE);
        if (!fluidStack.isEmpty()) {
            FluidType type = fluidStack.getFluid().getFluidType();
            IClientFluidTypeExtensions clientType = IClientFluidTypeExtensions.of(type);
            TextureAtlasSprite sprite = getSprite(clientType.getStillTexture(fluidStack));
            if (flowing) sprite = getSprite(clientType.getFlowingTexture(fluidStack));
            
            builder.setFirstUV(sprite.getU0(), sprite.getV0(), sprite.getU0() + ((sprite.getU1() - sprite.getU0()) * texLength), sprite.getV0() + ((sprite.getV1() - sprite.getV0()) * texWidth))
                .setSecondUV(sprite.getU0(), sprite.getV0(), sprite.getU0() + ((sprite.getU1() - sprite.getU0()) * texWidth), sprite.getV0() + ((sprite.getV1() - sprite.getV0()) * texHeight))
                .setThirdUV(sprite.getU0(), sprite.getV0(), sprite.getU0() + ((sprite.getU1() - sprite.getU0()) * texLength), sprite.getV0() + ((sprite.getV1() - sprite.getV0()) * texHeight))
                .setColor(ColorHelper.getColor(clientType.getTintColor(fluidStack)))
                .setLight(Math.max(type.getLightLevel(fluidStack) << 4, light));
        }
        return builder;
    }
    
    public static void renderConnectLine(PoseStack stack, Vec3 from, Vec3 to, Color color, float alpha) {
        double dX = to.x() - from.x();
        double dY = to.y() - from.y();
        double dZ = to.z() - from.z();
        
        double yaw = Math.atan2(dZ, dX);
        double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;
        
        stack.pushPose();
        stack.mulPose(Axis.YP.rotationDegrees((float) Math.toDegrees(-yaw)));
        stack.mulPose(Axis.ZP.rotationDegrees((float) Math.toDegrees(-pitch) - 180f));
        RenderBuilder.create().setRenderType(FluffyFurRenderTypes.ADDITIVE)
            .setColor(color)
            .setAlpha(alpha)
            .renderRay(stack, 0.01f, (float) from.distanceTo(to) + 0.01f);
        stack.popPose();
    }
    
    public static void renderConnectLine(PoseStack stack, BlockPos posFrom, BlockPos posTo, Color color, float alpha) {
        renderConnectLine(stack, posFrom.getCenter(), posTo.getCenter(), color, alpha);
    }
    
    public static void renderConnectLineOffset(PoseStack stack, Vec3 from, Vec3 to, Color color, float alpha) {
        stack.pushPose();
        stack.translate(from.x(), from.y(), from.z());
        renderConnectLine(stack, from, to, color, alpha);
        stack.popPose();
    }
    
    public static void renderConnectBoxLines(PoseStack stack, Vec3 size, Color color, float alpha) {
        renderConnectLineOffset(stack, new Vec3(0, 0, 0), new Vec3(size.x() , 0, 0), color, alpha);
        renderConnectLineOffset(stack, new Vec3(size.x(), 0, 0), new Vec3(size.x(), 0, size.z()), color, alpha);
        renderConnectLineOffset(stack, new Vec3(size.x(), 0, size.z()), new Vec3(0, 0, size.z()), color, alpha);
        renderConnectLineOffset(stack, new Vec3(0, 0, size.z()), new Vec3(0, 0, 0), color, alpha);
        
        renderConnectLineOffset(stack, new Vec3(0, 0, 0), new Vec3(0, size.y(), 0), color, alpha);
        renderConnectLineOffset(stack, new Vec3(size.x(), 0, 0), new Vec3(size.x(), size.y(), 0), color, alpha);
        renderConnectLineOffset(stack, new Vec3(size.x(), 0, size.z()), new Vec3(size.x(), size.y(), size.z()), color, alpha);
        renderConnectLineOffset(stack, new Vec3(0, 0, size.z()), new Vec3(0, size.y(), size.z()), color, alpha);
        
        renderConnectLineOffset(stack, new Vec3(0, size.y(), 0), new Vec3(size.x(), size.y(), 0), color, alpha);
        renderConnectLineOffset(stack, new Vec3(size.x(), size.y(), 0), new Vec3(size.x() , size.y(), size.z()), color, alpha);
        renderConnectLineOffset(stack, new Vec3(size.x(), size.y(), size.z()), new Vec3(0, size.y(), size.z()), color, alpha);
        renderConnectLineOffset(stack, new Vec3(0, size.y(), size.z()), new Vec3(0, size.y(), 0), color, alpha);
        stack.pushPose();
        stack.translate(0.01f, 0.01f, 0.01f);
        RenderBuilder.create().setRenderType(FluffyFurRenderTypes.ADDITIVE)
            .setColor(color)
            .setAlpha(alpha / 8f)
            .enableSided()
            .renderCube(stack, (float) size.x() - 0.02f, (float) size.y() - 0.02f, (float) size.z() - 0.02f);
        stack.popPose();
    }
    
    public static void renderConnectSideLines(PoseStack stack, Vec3 size, Color color, float alpha) {
        renderConnectLineOffset(stack, new Vec3(0, 0, 0), new Vec3(size.x() , 0, 0), color, alpha);
        renderConnectLineOffset(stack, new Vec3(size.x(), 0, 0), new Vec3(size.x(), 0, size.z()), color, alpha);
        renderConnectLineOffset(stack, new Vec3(size.x(), 0, size.z()), new Vec3(0, 0, size.z()), color, alpha);
        renderConnectLineOffset(stack, new Vec3(0, 0, size.z()), new Vec3(0, 0, 0), color, alpha);
        stack.pushPose();
        stack.mulPose(Axis.XP.rotationDegrees(90f));
        RenderBuilder.create().setRenderType(FluffyFurRenderTypes.ADDITIVE)
            .setColor(color)
            .setAlpha(alpha / 8f)
            .enableSided()
            .renderQuad(stack, (float) size.x(), (float) size.y());
        stack.popPose();
    }
    
    public static void renderConnectSide(PoseStack stack, Direction side, Color color, float alpha) {
        Vec3 size = new Vec3(1, 1, 1);
        stack.pushPose();
        stack.translate(0.5f, 0.5f, 0.5f);
        stack.mulPose(side.getOpposite().getRotation());
        stack.translate(0, -0.001f, 0);
        stack.translate(-size.x() / 2f, -size.y() / 2f, -size.z() / 2f);
        renderConnectSideLines(stack, size, color, alpha);
        stack.popPose();
    }
    
    public static boolean isFormulaLine(double f, double j, boolean limit, double l) {
        if (limit) {
            return f >= j - l && f <= j + l;
        }
        return false;
    }
    
    public static Vector3f parametricSphere(float u, float v, float r) {
        return new Vector3f(Mth.cos(u) * Mth.sin(v) * r, Mth.cos(v) * r, Mth.sin(u) * Mth.sin(v) * r);
    }
    
    public static Vec2 perpendicularTrailPoints(Vector4f start, Vector4f end, float width) {
        float x = -start.x();
        float y = -start.y();
        if (Math.abs(start.z()) > 0) {
            float ratio = end.z() / start.z();
            x = end.x() + x * ratio;
            y = end.y() + y * ratio;
        } else if (Math.abs(end.z()) <= 0) {
            x += end.x();
            y += end.y();
        }
        if (start.z() > 0) {
            x = -x;
            y = -y;
        }
        if (x * x + y * y > 0F) {
            float normalize = width * 0.5F / distance(x, y);
            x *= normalize;
            y *= normalize;
        }
        return new Vec2(-y, x);
    }
    
    public static float distance(float... a) {
        return sqrt(distSqr(a));
    }
    
    public static float distSqr(float... a) {
        float d = 0.0F;
        for (float f : a) {
            d += f * f;
        }
        return d;
    }
    
    public static void applyWobble(Vector3f[] offsets, float strength, float gameTime) {
        float offset = 0;
        for (Vector3f vector3f : offsets) {
            double time = ((gameTime / 40.0F) % Math.PI * 2);
            float sine = Mth.sin((float) (time + (offset * Math.PI * 2))) * strength;
            vector3f.add(sine, -sine, 0);
            offset += 0.25f;
        }
    }
    
    public static void renderModel(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Model model, float red, float green, float blue, float alpha, RenderType renderType) {
        VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
        if(red<0 || red>1) throw new IllegalArgumentException("0<=red<=1, red="+ red);
        if(green<0 || green>1) throw new IllegalArgumentException("0<=green<=1, green="+green);
        if(blue<0 || blue>1) throw new IllegalArgumentException("0<=blue<=1, blue="+blue);
        if(alpha<0 || alpha>1) throw new IllegalArgumentException("0<=alpha<=1, alpha="+alpha);
        model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
    }
    
    public static SplitText splitText(String text, int max, float scale) {
        SplitText result = new SplitText(scale);
        StringBuilder currentLine = new StringBuilder();
        int currentWidth = 0;
        Font font = Minecraft.getInstance().font;
        
        String[] words = text.split(" ");
        for (String word : words) {
            int wordWidth = (int) (font.width(word + " ")*scale);
            if(word.equals("\n") || currentWidth+wordWidth>max){
                result.add(currentLine.toString());
                currentWidth=word.equals("\n") ? 0 : wordWidth;
                currentLine=new StringBuilder();
                if(!word.equals("\n")) currentLine.append(word).append(" ");
            } else {
                currentWidth += wordWidth;
                currentLine.append(word).append(" ");
            }
        }
        
        return result;
    }
    public static SplitText splitText(String text, int max) {
        return splitText(text, max, 1);
    }
    
    public static String toString(List<Component> components){
        StringBuilder builder = new StringBuilder();
        for(Component component : components)
            builder.append(component.getString()).append(" \n ");
        return builder.toString();
    }
}