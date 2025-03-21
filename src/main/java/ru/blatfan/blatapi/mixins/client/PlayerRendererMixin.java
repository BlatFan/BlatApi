package ru.blatfan.blatapi.mixins.client;

import ru.blatfan.blatapi.fluffy_fur.client.render.entity.ExtraLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {

    @Inject(at = @At("RETURN"), method = "<init>")
    private void fluffy_fur$PlayerRenderer(EntityRendererProvider.Context context, boolean useSlimModel, CallbackInfo ci) {
        PlayerRenderer self = (PlayerRenderer) ((Object) this);
        self.addLayer(new ExtraLayer<>(self));
    }
}
