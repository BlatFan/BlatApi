package ru.blatfan.blatapi.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        
        guiGraphics.blit(skinTexture, x, y, size, size, 8.0f, 8.0f, 8, 8, 64, 64);
        guiGraphics.blit(skinTexture, x, y, size, size, 40.0f, 8.0f, 8, 8, 64, 64);
        
        RenderSystem.disableBlend();
    }
    public static void renderFlatPlayerHead(GuiGraphics guiGraphics, int x, int y, int size, Player player) {
        ResourceLocation skinTexture = Minecraft.getInstance().getSkinManager().getInsecureSkinLocation(player.getGameProfile());
        
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        
        guiGraphics.blit(skinTexture, x, y, size, size, 8.0f, 8.0f, 8, 8, 64, 64);
        guiGraphics.blit(skinTexture, x, y, size, size, 40.0f, 8.0f, 8, 8, 64, 64);
        
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
        
        float s = 4.0f;
        int light = 15728880;
        Matrix4f m = poseStack.last().pose();
        Matrix3f n = poseStack.last().normal();
        int overlay = LivingEntityRenderer.getOverlayCoords(player, 0.0f);
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
        
        u1 = 8f/64f; v1 = 0f/64f; u2 = 16f/64f; v2 = 8f/64f;
        buffer.vertex(m, -s, -s, -s).color(255,255,255,255).uv(u1,v1).overlayCoords(overlay).uv2(light).normal(n, 0,-1,0).endVertex();
        buffer.vertex(m,  s, -s, -s).color(255,255,255,255).uv(u2,v1).overlayCoords(overlay).uv2(light).normal(n, 0,-1,0).endVertex();
        buffer.vertex(m,  s, -s,  s).color(255,255,255,255).uv(u2,v2).overlayCoords(overlay).uv2(light).normal(n, 0,-1,0).endVertex();
        buffer.vertex(m, -s, -s,  s).color(255,255,255,255).uv(u1,v2).overlayCoords(overlay).uv2(light).normal(n, 0,-1,0).endVertex();
        
        u1 = 16f/64f; v1 = 0f/64f; u2 = 24f/64f; v2 = 8f/64f;
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
        if(ingredient.values[0] instanceof Ingredient.TagValue tagValue)
            components.add(1, Component.literal("Tag: #"+ tagValue.tag.location()));
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
    
    public static void drawGradientRect(Matrix4f mat, int zLevel, int left, int top, int right, int bottom, int startColor, int endColor)
    {
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
    
    public static void drawGradientRect(Matrix4f mat, BufferBuilder bufferBuilder, int left, int top, int right, int bottom, int zLevel, int startColor, int endColor)
    {
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
    
    public static void drawGradientRectHorizontal(Matrix4f mat, int zLevel, int left, int top, int right, int bottom, int startColor, int endColor)
    {
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
    
    private static void innerBlit(PoseStack poseStack, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1)
    {
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