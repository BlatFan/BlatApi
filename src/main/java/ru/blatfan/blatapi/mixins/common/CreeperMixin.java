package ru.blatfan.blatapi.mixins.common;

import ru.blatfan.blatapi.fluffy_fur.common.fire.FireItemHandler;
import ru.blatfan.blatapi.fluffy_fur.common.fire.FireItemModifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Creeper.class)
public abstract class CreeperMixin {

    @Inject(method = "mobInteract", at = @At("RETURN"), cancellable = true)
    private void fluffy_fur$mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        Creeper self = (Creeper) ((Object) this);
        for (FireItemModifier modifier : FireItemHandler.getModifiers()) {
            if (modifier.isCreeperInteract(self, player, hand)) {
                modifier.creeperInteract(self, player, hand);
                self.ignite();
                cir.setReturnValue(InteractionResult.sidedSuccess(self.level().isClientSide()));
            }
        }
    }
}
