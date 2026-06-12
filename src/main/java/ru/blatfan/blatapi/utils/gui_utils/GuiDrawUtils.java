package ru.blatfan.blatapi.utils.gui_utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import lombok.experimental.UtilityClass;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

import static ru.blatfan.blatapi.utils.gui_utils.GuiRenderUtil.testColorValues;

@UtilityClass
@SuppressWarnings("ALL")
public class GuiDrawUtils {
    public static void blit(GuiGraphics gui, float scale, int x, int y, int pBlitOffset, int pWidth, int pHeight, TextureAtlasSprite pSprite) {
        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.scale(scale, scale, 1);
        gui.blit((int) (x/scale), (int) (y/scale), pBlitOffset, pWidth, pHeight, pSprite);
        pose.popPose();
    }
    
    public static void blitScaledTinted(GuiGraphics gui, float scale, int x, int y, int pBlitOffset, int pWidth, int pHeight, TextureAtlasSprite pSprite, float pRed, float pGreen, float pBlue, float pAlpha) {
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
    
    public static void blit(GuiGraphics gui, ResourceLocation pAtlasLocation, float x, float y, float pUOffset, float pVOffset, int pWidth, int pHeight, int pTextureWidth, int pTextureHeight, float red, float green, float blue, float alpha) {
        blit(gui, pAtlasLocation, x, y, x+pWidth, y+pHeight, pUOffset/pTextureWidth, (pUOffset+pWidth)/pTextureWidth, pVOffset/pTextureHeight, (pVOffset+pWidth)/pTextureHeight, red, green, blue, alpha);
    }
    
    public void blit(GuiGraphics gui, int x, int y, int width, int height, TextureAtlasSprite sprite, float red, float green, float blue, float alpha) {
        blit(gui, sprite.atlasLocation(), x, y, x + width, y + height, sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(), red, green, blue, alpha);
    }
    
    public static void blit(GuiGraphics gui, ResourceLocation pAtlasLocation, float x0, float y0, float x1, float y1, float u0, float u1, float v0, float v1, float red, float green, float blue, float alpha) {
        testColorValues(red, green, blue, alpha);
        RenderSystem.setShaderTexture(0, pAtlasLocation);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f matrix4f = gui.pose().last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f, x0, y0, 0).uv(u0, v0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.vertex(matrix4f, x0, y1, 0).uv(u0, v1).color(red, green, blue, alpha).endVertex();
        bufferbuilder.vertex(matrix4f, x1, y0, 0).uv(u1, v0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.vertex(matrix4f, x1, y1, 0).uv(u1, v1).color(red, green, blue, alpha).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }
    
    public static void nineSlice(GuiGraphics gui, float scale, ResourceLocation texture, int x, int y, int width, int height, int sliceSize, int uWidth, int vHeight, int uOffset, int vOffset,
                                           int textureWidth, int textureHeight) {
        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.scale(scale, scale, 1);
        gui.blitNineSlicedSized(texture, (int) (x/scale), (int) (y/scale), width, height, sliceSize, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight);
        pose.popPose();
    }
    
    public static void nineSlice(GuiGraphics gui, float scale, ResourceLocation texture, int x, int y, int width, int height, int sliceWidth, int sliceHeight, int uWidth, int vHeight,
                                           int uOffset, int vOffset, int textureWidth, int textureHeight) {
        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.scale(scale, scale, 1);
        gui.blitNineSlicedSized(texture, (int) (x/scale), (int) (y/scale), width, height, sliceWidth, sliceHeight, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight);
        pose.popPose();
    }
    
    public static void nineSlice(GuiGraphics gui, float scale, ResourceLocation texture, int x, int y, int width, int height, int cornerWidth, int cornerHeight, int edgeWidth, int edgeHeight,
                                           int uWidth, int vHeight, int uOffset, int vOffset, int textureWidth, int textureHeight) {
        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.scale(scale, scale, 1);
        gui.blitNineSlicedSized(texture, (int) (x/scale), (int) (y/scale), width, height, cornerWidth, cornerHeight, edgeWidth, edgeHeight, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight);
        pose.popPose();
    }
    
    public static void innerBlit(GuiGraphics gui, ResourceLocation pAtlasLocation, int pX1, int pX2, int pY1, int pY2, int pBlitOffset, float pMinU, float pMaxU, float pMinV, float pMaxV) {
        RenderSystem.setShaderTexture(0, pAtlasLocation);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f matrix4f = gui.pose().last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f, (float)pX1, (float)pY1, (float)pBlitOffset).uv(pMinU, pMinV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pX1, (float)pY2, (float)pBlitOffset).uv(pMinU, pMaxV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pX2, (float)pY2, (float)pBlitOffset).uv(pMaxU, pMaxV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pX2, (float)pY1, (float)pBlitOffset).uv(pMaxU, pMinV).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }
}
