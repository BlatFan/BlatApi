package ru.blatfan.blatapi.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.world.entity.LightningBolt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.blatfan.blatapi.client.effect.BAEffects;
import ru.blatfan.blatapi.config.BlatApiClientConfig;

@Mixin(LightningBoltRenderer.class)
public abstract class LightningBoltRendererMixin {

    @Inject(at = @At("HEAD"), method = "render*", cancellable = true)
    public void blatapi$render(LightningBolt entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (BlatApiClientConfig.LIGHTNING_BOLT_EFFECT.get()) {
            ci.cancel();
            BAEffects.lightningBoltRender(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
    }
}
