package ru.blatfan.blatapi.mixins.client;

import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.blatfan.blatapi.client.effect.BAEffects;
import ru.blatfan.blatapi.config.BlatApiClientConfig;

@Mixin(LightningBolt.class)
public abstract class LightningBoltMixin {

    @Shadow
    private int life;

    @Inject(at = @At("HEAD"), method = "tick")
    public void blatapi$tick(CallbackInfo ci) {
        LightningBolt self = (LightningBolt) ((Object) this);
        Level level = self.level();
        if (level.isClientSide()) {
            if (BlatApiClientConfig.LIGHTNING_BOLT_EFFECT.get()) {
                Vec3 pos = self.position();
                if (life == 2) {
                    BAEffects.lightningBoltSpawnEffect(level, pos);
                }
                BAEffects.lightningBoltTickEffect(level, pos);
            }
        }
    }
}
