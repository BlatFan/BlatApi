package ru.blatfan.blatapi.utils.gui_utils;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.UUID;

import static ru.blatfan.blatapi.utils.gui_utils.GuiRenderUtil.FULL_BRIGHT;

@UtilityClass
@SuppressWarnings("ALL")
public class GuiEntityUtil {
    public static GameProfile createOfflineProfile(String playerName) {
        UUID offlineUUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes());
        return createOfflineProfile(offlineUUID, playerName);
    }
    public static GameProfile createOfflineProfile(UUID uuid, String playerName) {
        return new GameProfile(uuid, playerName);
    }
    public static float getPartialTick(){
        return Minecraft.getInstance().getPartialTick();
    }
    public static void renderFlatPlayerHead(GuiGraphics gui, int x, int y, int size, String player) {
        renderFlatPlayerHead(gui, x, y, size, createOfflineProfile(player));
    }
    public static void renderFlatPlayerHead(GuiGraphics gui, int x, int y, int size, GameProfile player) {
        ResourceLocation skinTexture = Minecraft.getInstance().getSkinManager().getInsecureSkinLocation(player);
        
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        
        gui.blit(skinTexture, x, y, size, size, 8f, 8f, 8, 8, 64, 64);
        gui.blit(skinTexture, x, y, size, size, 40f, 8f, 8, 8, 64, 64);
        
        RenderSystem.disableBlend();
    }
    public static void renderFlatPlayerHead(GuiGraphics gui, int x, int y, int size, Player player) {
        renderFlatPlayerHead(gui, x, y, size, player.getGameProfile());
    }
    
    public static void render3DPlayerHeadMouse(GuiGraphics gui, int x, int y, int size, float mouseX, float mouseY, Player player) {
        render3DPlayerHeadMouse(gui, x, y, size, mouseX, mouseY, player.getGameProfile());
    }
    public static void render3DPlayerHeadMouse(GuiGraphics gui, int x, int y, int size, float mouseX, float mouseY, GameProfile player) {
        render3DPlayerHead(gui, x, y, size, (mouseX - x) * 0.1f, (mouseY - y) * 0.25f, player);
    }
    
    public static void render3DPlayerHead(GuiGraphics gui, int x, int y, int size, float degreeX, float degreeY, Player player) {
        render3DPlayerHead(gui, x, y, size, degreeX, degreeY, player.getGameProfile());
    }
    public static void render3DPlayerHead(GuiGraphics gui, int x, int y, int size, float degreeX, float degreeY, GameProfile player) {
        PoseStack poseStack = gui.pose();
        poseStack.pushPose();
        
        poseStack.translate(x, y, 100);
        poseStack.scale(size, size, size);
        
        poseStack.mulPose(Axis.YP.rotationDegrees(degreeX));
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        poseStack.mulPose(Axis.XP.rotationDegrees(-degreeY));
        
        renderPlayerHeadCube(player, poseStack);
        
        poseStack.popPose();
    }
    
    private static void renderPlayerHeadCube(GameProfile player, PoseStack poseStack) {
        ResourceLocation skinTexture = Minecraft.getInstance().getSkinManager().getInsecureSkinLocation(player);
        
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderType renderType = RenderType.entityCutoutNoCull(skinTexture);
        VertexConsumer buffer = bufferSource.getBuffer(renderType);
        
        float s = 4f;
        int light = FULL_BRIGHT;
        Matrix4f m = poseStack.last().pose();
        Matrix3f n = poseStack.last().normal();
        int overlay = LivingEntityRenderer.getOverlayCoords(Minecraft.getInstance().player, 0f);
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
    
    public static EntityRotationState prepareForGuiPreview(Entity entity) {
        EntityRotationState state = new EntityRotationState();
        
        state.yRot = entity.getYRot();
        state.yRotO = entity.yRotO;
        state.xRot = entity.getXRot();
        state.xRotO = entity.xRotO;
        
        entity.setYRot(0);
        entity.yRotO = 0;
        entity.setXRot(0);
        entity.xRotO = 0;
        
        if (entity instanceof LivingEntity living) {
            state.yBodyRot = living.yBodyRot;
            state.yBodyRotO = living.yBodyRotO;
            state.yHeadRot = living.yHeadRot;
            state.yHeadRotO = living.yHeadRotO;
            
            living.yBodyRot = 0;
            living.yBodyRotO = 0;
            living.yHeadRot = 0;
            living.yHeadRotO = 0;
        }
        
        return state;
    }
    
    public static void renderEntityFollowsMouse(GuiGraphics gui, int x, int y, int scale, float mouseX, float mouseY, Entity entity){
        float f = (float)Math.atan(mouseX / 40);
        float f1 = (float)Math.atan(mouseY / 40);
        renderEntityWithCameraRotation(gui, x, y, scale, f, f1, entity);
    }
    public static void renderEntityFollowsMouseAuto(GuiGraphics gui, int x, int y, int scale, float mouseX, float mouseY, Entity entity){
        renderEntityFollowsMouse(gui, x, y, scale, x-mouseX, y-mouseY, entity);
    }
    public static void renderEntityWithCameraRotation(GuiGraphics gui, int x, int y, int scale, float lookX, float lookY, Entity entity) {
        EntityRotationState state = prepareForGuiPreview(entity);
        
        try {
            Quaternionf cameraRotation = Axis.XP.rotationDegrees(lookY * 20);
            Quaternionf poseRotation = Axis.ZP.rotationDegrees(180);
            
            float baseYaw = 180;
            float headYawOffset = lookX * 20;
            float pitch = -lookY * 20;
            
            if(entity instanceof LivingEntity living){
                living.yBodyRot = baseYaw;
                living.yBodyRotO = baseYaw;
            }
            
            entity.setYRot(baseYaw);
            entity.yRotO = baseYaw;
            
            entity.setXRot(pitch);
            entity.xRotO = pitch;
            
            if(entity instanceof LivingEntity living){
                living.yHeadRot = baseYaw + headYawOffset;
                living.yHeadRotO = baseYaw + headYawOffset;
            }
            
            renderEntity(gui, x, y, scale, poseRotation, cameraRotation, false, entity);
        } finally {
            state.set(entity);
        }
    }
    public static void renderEntityQuaternionf(GuiGraphics gui, int x, int y, int scale, Quaternionf quaternionf, Quaternionf quaternionf1, Entity entity){
        renderEntity(gui, x, y, scale, new Quaternionf(quaternionf).mul(quaternionf1), quaternionf1, true, entity);
    }
    public static void renderEntity(GuiGraphics gui, int x, int y, float scale, Entity entity) {
        renderEntity(gui, x, y, scale, new Quaternionf(), defaultGuiCameraRotation(), true, entity);
    }
    public static void renderEntity(GuiGraphics gui, int x, int y, float scale, Quaternionf poseRotation, Entity entity) {
        renderEntity(gui, x, y, scale, poseRotation, defaultGuiCameraRotation(), true, entity);
    }
    public static void renderEntity(GuiGraphics gui, int x, int y, float scale, Quaternionf poseRotation, Quaternionf cameraRotation, LivingEntity entity) {
        renderEntity(gui, x, y, scale, poseRotation, cameraRotation, false, entity);
    }
    public static void renderEntity(GuiGraphics gui, int x, int y, float scale, Quaternionf poseRotation, Quaternionf cameraRotation, boolean applyDefaultGuiOrientation, Entity entity) {
        if (entity == null) return;
        
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        Quaternionf cameraOrientation0 = dispatcher.cameraOrientation();
        
        try {
            gui.pose().pushPose();
            gui.pose().translate(x, y, 50);
            gui.pose().mulPoseMatrix(new Matrix4f().scaling(scale, scale, -scale));
            if (applyDefaultGuiOrientation) {
                gui.pose().mulPose(Axis.ZP.rotationDegrees(180));
                gui.pose().mulPose(Axis.YP.rotationDegrees(180));
            }
            gui.pose().mulPose(poseRotation);
            
            Lighting.setupForEntityInInventory();
            dispatcher.setRenderShadow(false);
            
            if (cameraRotation != null)
                dispatcher.overrideCameraOrientation(new Quaternionf(cameraRotation).conjugate());
            
            dispatcher.render(entity, 0, 0, 0, 0, Minecraft.getInstance().getFrameTime(), gui.pose(), gui.bufferSource(), FULL_BRIGHT);
            
            gui.flush();
        } finally {
            dispatcher.setRenderShadow(true);
            dispatcher.overrideCameraOrientation(cameraOrientation0);
            gui.pose().popPose();
            Lighting.setupFor3DItems();
        }
    }
    public static Quaternionf defaultGuiCameraRotation() {
        return Axis.XP.rotationDegrees(180);
    }
    public static float computeAutoScale(Entity entity, float maxSize){
        float baseScale = maxSize;
        float es = Math.max(entity.getBbWidth(), entity.getBbHeight());
        return (es >= 0.7f) ? (baseScale / (es + 1)) : (baseScale * 1.5f);
    }
}