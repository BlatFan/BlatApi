package ru.blatfan.blatapi.mixins.client;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.blatfan.blatapi.common.BARegistry;
import ru.blatfan.blatapi.utils.ClientTicks;
import ru.blatfan.blatapi.utils.ColorHelper;

@Mixin(PotionUtils.class)
public class PotionUtilsMixin {
    
    @Inject(at = @At("RETURN"), method = "getColor(Lnet/minecraft/world/item/ItemStack;)I", cancellable = true)
    private static void rainbow(ItemStack pStack, CallbackInfoReturnable<Integer> cir){
        if(cir.getReturnValue()==BARegistry.POTION_RAINBOW_COLOR)
            cir.setReturnValue(ColorHelper.rainbowColor(ClientTicks.ticks/10f).getRGB());
    }
}