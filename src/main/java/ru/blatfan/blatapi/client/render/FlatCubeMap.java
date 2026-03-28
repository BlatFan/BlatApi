package ru.blatfan.blatapi.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@OnlyIn(Dist.CLIENT)
public class FlatCubeMap extends CubeMap {
    private final ResourceLocation texture;
    
    public FlatCubeMap(ResourceLocation texture) {
        super(texture);
        this.texture = texture;
        for (int i = 0; i < 6; ++i)
            this.images[i] = texture;
    }
    
    @Override
    public CompletableFuture<Void> preload(TextureManager textureManager, Executor executor) {
        return textureManager.preload(this.texture, executor);
    }
    
    @Override
    public void render(Minecraft mc, float pitch, float yaw, float alpha) {
        int width = mc.getWindow().getWidth();
        int height = mc.getWindow().getHeight();
        
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, this.texture);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);
        
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        
        Matrix4f projection = new Matrix4f().setOrtho(0, (float)width, (float)height, 0, 0.1F, 1000);
        RenderSystem.backupProjectionMatrix();
        RenderSystem.setProjectionMatrix(projection, VertexSorting.ORTHOGRAPHIC_Z);
        
        PoseStack poseStack = RenderSystem.getModelViewStack();
        poseStack.pushPose();
        poseStack.setIdentity();
        poseStack.translate(0, 0, -500);
        RenderSystem.applyModelViewMatrix();
        
        int colorAlpha = Math.round(255 * alpha);
        
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferBuilder.vertex(0, height, 0).uv(0, 1).color(255, 255, 255, colorAlpha).endVertex();
        bufferBuilder.vertex(width, height, 0).uv(1, 1).color(255, 255, 255, colorAlpha).endVertex();
        bufferBuilder.vertex(width, 0, 0).uv(1, 0).color(255, 255, 255, colorAlpha).endVertex();
        bufferBuilder.vertex(0, 0, 0).uv(0, 0).color(255, 255, 255, colorAlpha).endVertex();
        tesselator.end();
        
        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.restoreProjectionMatrix();
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
    }
}
