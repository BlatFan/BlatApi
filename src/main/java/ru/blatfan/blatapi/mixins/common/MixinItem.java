package ru.blatfan.blatapi.mixins.common;

import ru.blatfan.blatapi.common.BARegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.extensions.IForgeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.blatfan.blatapi.utils.DisabledRecipes;

@Mixin(Item.class)
public abstract class MixinItem implements FeatureElement, ItemLike, IForgeItem {
    @Inject(at = @At("RETURN"), method = "isValidRepairItem", cancellable = true)
    public void isValdRepairItem(ItemStack stack, ItemStack repairCandidate, CallbackInfoReturnable<Boolean> callback) {
        if (DisabledRecipes.isRepairItemDisabled(repairCandidate) || DisabledRecipes.isRepairDisabled(stack, repairCandidate))
            callback.setReturnValue(false);

        var level = Minecraft.getInstance().level;

        if (level != null) {
            var container = new SimpleContainer(2);
            container.addItem(stack);
            container.addItem(repairCandidate);

            if (level.getRecipeManager().getRecipeFor(BARegistry.ANVIL_REPAIR.get(), container, level).isPresent())
                callback.setReturnValue(true);
        }
    }
}
