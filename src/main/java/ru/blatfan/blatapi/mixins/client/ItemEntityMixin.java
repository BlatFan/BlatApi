package ru.blatfan.blatapi.mixins.client;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.common.item.IParticleItem;
import ru.blatfan.blatapi.config.BlatApiClientConfig;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow
    public abstract ItemStack getItem();

    @Inject(at = @At("RETURN"), method = "tick")
    public void blatapi$addParticles(CallbackInfo ci) {
        ItemEntity self = (ItemEntity) ((Object) this);
        if (self.level().isClientSide()) {
            if (BlatApiClientConfig.ITEM_PARTICLE.get()) {
                if (self.getItem().getItem() instanceof IParticleItem) {
                    IParticleItem item = (IParticleItem) self.getItem().getItem();
                    item.addParticles(BlatApi.proxy.getLevel(), self);
                }
            }
        }
    }
}
