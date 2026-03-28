package ru.blatfan.blatapi.mixins.common;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.blatfan.blatapi.utils.BlockedStageHelper;

@Mixin(Slot.class)
public abstract class SlotMixin {
    @Shadow
    @Final
    public Container container;
    
    @Shadow
    public abstract ItemStack getItem();
    
    @Inject(at = @At("RETURN"), method = "mayPickup", cancellable = true)
    private void pickup(Player pPlayer, CallbackInfoReturnable<Boolean> cir){
        if(container instanceof Inventory) return;
        if(!BlockedStageHelper.canUse(pPlayer, getItem()))
            cir.setReturnValue(false);
    }
}