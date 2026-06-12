package ru.blatfan.blatapi.utils.gui_utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import lombok.experimental.UtilityClass;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import ru.blatfan.blatapi.client.registry.BARenderTypes;
import ru.blatfan.blatapi.client.render.RenderBuilder;

import java.awt.*;

import static net.minecraft.util.Mth.sqrt;

@UtilityClass
@SuppressWarnings("ALL")
public class GuiPrimitiveUtil {
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
        float startAlpha = (float)(startColor >> 24 & 255) / 255;
        float startRed   = (float)(startColor >> 16 & 255) / 255;
        float startGreen = (float)(startColor >>  8 & 255) / 255;
        float startBlue  = (float)(startColor       & 255) / 255;
        float endAlpha   = (float)(endColor   >> 24 & 255) / 255;
        float endRed     = (float)(endColor   >> 16 & 255) / 255;
        float endGreen   = (float)(endColor   >>  8 & 255) / 255;
        float endBlue    = (float)(endColor         & 255) / 255;
        
        bufferBuilder.vertex(mat, right,    top, zLevel).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        bufferBuilder.vertex(mat,  left,    top, zLevel).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        bufferBuilder.vertex(mat,  left, bottom, zLevel).color(  endRed,   endGreen,   endBlue,   endAlpha).endVertex();
        bufferBuilder.vertex(mat, right, bottom, zLevel).color(  endRed,   endGreen,   endBlue,   endAlpha).endVertex();
    }
    
    public static void drawGradientRectHorizontal(Matrix4f mat, int zLevel, int left, int top, int right, int bottom, int startColor, int endColor) {
        float startAlpha = (float)(startColor >> 24 & 255) / 255;
        float startRed   = (float)(startColor >> 16 & 255) / 255;
        float startGreen = (float)(startColor >>  8 & 255) / 255;
        float startBlue  = (float)(startColor       & 255) / 255;
        float endAlpha   = (float)(endColor   >> 24 & 255) / 255;
        float endRed     = (float)(endColor   >> 16 & 255) / 255;
        float endGreen   = (float)(endColor   >>  8 & 255) / 255;
        float endBlue    = (float)(endColor         & 255) / 255;
        
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
        innerBlit(poseStack, x0, x1, y0, y1, z, (texX + 0) / (float)fullWidth, (texX + (float)texWidth) / (float)fullWidth, (texY + 0) / (float)fullHeight, (texY + (float)texHeight) / (float)fullHeight);
    }
    
    public static void drawTextureWithMasking(Matrix4f matrix, float xCoord, float yCoord, TextureAtlasSprite textureSprite, float maskTop, float maskRight) {
        float uMin = textureSprite.getU0();
        float uMax = textureSprite.getU1();
        float vMin = textureSprite.getV0();
        float vMax = textureSprite.getV1();
        uMax -= maskRight / 16 * (uMax - uMin);
        vMax -= maskTop / 16 * (vMax - vMin);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix, xCoord, yCoord + 16, (float) 0.0).uv(uMin, vMax).endVertex();
        bufferBuilder.vertex(matrix, xCoord + 16 - maskRight, yCoord + 16, (float) 0.0).uv(uMax, vMax).endVertex();
        bufferBuilder.vertex(matrix, xCoord + 16 - maskRight, yCoord + maskTop, (float) 0.0).uv(uMax, vMin).endVertex();
        bufferBuilder.vertex(matrix, xCoord, yCoord + maskTop, (float) 0.0).uv(uMin, vMin).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());
    }
    
    public static  void innerBlit(PoseStack poseStack, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1) {
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
    
    public static void renderConnectLine(PoseStack stack, Vec3 from, Vec3 to, Color color, float alpha) {
        double dX = to.x() - from.x();
        double dY = to.y() - from.y();
        double dZ = to.z() - from.z();
        
        double yaw = Math.atan2(dZ, dX);
        double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;
        
        stack.pushPose();
        stack.mulPose(Axis.YP.rotationDegrees((float) Math.toDegrees(-yaw)));
        stack.mulPose(Axis.ZP.rotationDegrees((float) Math.toDegrees(-pitch) - 180f));
        RenderBuilder.create().setRenderType(BARenderTypes.ADDITIVE)
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
        RenderBuilder.create().setRenderType(BARenderTypes.ADDITIVE)
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
        RenderBuilder.create().setRenderType(BARenderTypes.ADDITIVE)
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
        float d = 0;
        for (float f : a) {
            d += f * f;
        }
        return d;
    }
    
    public static void applyWobble(Vector3f[] offsets, float strength, float gameTime) {
        float offset = 0;
        for (Vector3f vector3f : offsets) {
            double time = ((gameTime / 40) % Math.PI * 2);
            float sine = Mth.sin((float) (time + (offset * Math.PI * 2))) * strength;
            vector3f.add(sine, -sine, 0);
            offset += 0.25f;
        }
    }
}