package ru.blatfan.blatapi.mixins.client;

import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.blatfan.blatapi.utils.ClientTicks;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Inject(at = @At("HEAD"), method = "render(FJZ)V")
    public void renderHead(float tickDelta, long startTime, boolean tick, CallbackInfo info) {
        ClientTicks.renderTickStart(tickDelta);
    }
    
    @Inject(at = @At("RETURN"), method = "render(FJZ)V")
    public void renderReturn(float tickDelta, long startTime, boolean tick, CallbackInfo info) {
        ClientTicks.renderTickEnd();
    }
}