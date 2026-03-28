package ru.blatfan.blatapi.mixins.client;

import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.effect.BAEffects;
import ru.blatfan.blatapi.config.BlatApiClientConfig;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {

    @Inject(at = @At("RETURN"), method = "finalizeExplosion")
    public void blatapi$tick(boolean spawnParticles, CallbackInfo ci) {
        if (BlatApi.proxy.getLevel().isClientSide()) {
            if (BlatApiClientConfig.EXPLOSION_EFFECT.get()) {
                if (spawnParticles) {
                    Explosion self = (Explosion) ((Object) this);
                    BAEffects.explosionEffect(BlatApi.proxy.getLevel(), self.getPosition());
                }
            }
        }
    }
}
