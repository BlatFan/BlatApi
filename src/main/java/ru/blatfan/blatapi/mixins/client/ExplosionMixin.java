package ru.blatfan.blatapi.mixins.client;

import ru.blatfan.blatapi.fluffy_fur.FluffyFur;
import ru.blatfan.blatapi.fluffy_fur.client.effect.FluffyFurEffects;
import ru.blatfan.blatapi.fluffy_fur.config.FluffyFurClientConfig;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {

    @Inject(at = @At("RETURN"), method = "finalizeExplosion")
    public void fluffy_fur$tick(boolean spawnParticles, CallbackInfo ci) {
        if (FluffyFur.proxy.getLevel().isClientSide()) {
            if (FluffyFurClientConfig.EXPLOSION_EFFECT.get()) {
                if (spawnParticles) {
                    Explosion self = (Explosion) ((Object) this);
                    FluffyFurEffects.explosionEffect(FluffyFur.proxy.getLevel(), self.getPosition());
                }
            }
        }
    }
}
