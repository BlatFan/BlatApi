package ru.blatfan.blatapi.mixins.common;

import net.minecraft.world.item.crafting.RecipeType;
import ru.blatfan.blatapi.anvilapi.api.recipe.IAnvilRecipe;
import ru.blatfan.blatapi.common.BARegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.blatfan.blatapi.utils.DisabledRecipes;
import ru.blatfan.blatapi.utils.ItemHelper;

@Mixin(AnvilMenu.class)
public abstract class MixinAnvilMenu extends ItemCombinerMenu {
    @Final
    @Shadow
    private DataSlot cost;
    @Shadow
    private String itemName;
    @Shadow
    public int repairItemCountCost;

    public MixinAnvilMenu(@Nullable MenuType<?> type, int containerId, Inventory container, ContainerLevelAccess access) {
        super(type, containerId, container, access);
    }

    @Shadow
    public abstract void createResult();

    @Inject(at = @At("HEAD"), method = "mayPickup", cancellable = true)
    protected void mayPickup(Player player, boolean b, CallbackInfoReturnable<Boolean> callback) {
        callback.setReturnValue(player.getAbilities().instabuild || player.experienceLevel >= this.cost.get());

        callback.cancel();
    }

    @Inject(at = @At("HEAD"), method = "onTake", cancellable = true)
    protected void onTake(Player player, ItemStack stack, CallbackInfo callback) {
        callback.cancel();

        if (!player.getAbilities().instabuild)
            player.giveExperienceLevels(-this.cost.get());

        this.cost.set(0);

        var breakChance = ForgeHooks.onAnvilRepair(player, stack, this.inputSlots.getItem(0), this.inputSlots.getItem(1));

        var recipe = this.player.level().getRecipeManager().getRecipeFor(BARegistry.ANVIL.get(), this.inputSlots, this.player.level()).orElse(null);

        if (recipe != null) {
            var input1 = this.inputSlots.getItem(0);
            var input2 = this.inputSlots.getItem(1);

            var input1Slot = 0;
            var input2Slot = 1;

            if (!(recipe.getInput(0).test(this.inputSlots.getItem(0)) && recipe.getInput(1).test(this.inputSlots.getItem(1)))) {
                input1 = this.inputSlots.getItem(1);
                input2 = this.inputSlots.getItem(0);

                input1Slot = 1;
                input2Slot = 0;
            }

            if (recipe.isConsuming(0)) {
                if (recipe.isUsingDurability(0) && input1.isDamageableItem())
                    input1.hurtAndBreak(recipe.getInputCount(0), this.player, p -> this.access.execute(((level, pos) -> level.levelEvent(1029, pos, 0))));
                else
                    input1.shrink(recipe.getInputCount(0));
            }

            if (!recipe.getReturn(0).isEmpty()) {
                var returnItem = recipe.getReturn(0).copy();

                if (input1.isEmpty())
                    this.inputSlots.setItem(input1Slot, returnItem);
                else {
                    if (!recipe.isUsingDurability(0)) {
                        if (ItemHelper.canCombineStacks(input1, returnItem))
                            this.inputSlots.setItem(input1Slot, ItemHelper.combineStacks(input1, returnItem));

                        else
                            this.access.execute(((level, pos) -> Containers.dropItemStack(level, pos.getX(), pos.getY() + 1, pos.getZ(), returnItem)));
                    }
                }
            }

            if (recipe.isConsuming(1)) {
                if (recipe.isUsingDurability(1) && input2.isDamageableItem())
                    input2.hurtAndBreak(recipe.getInputCount(1), this.player, p -> this.access.execute(((level, pos) -> level.levelEvent(1029, pos, 0))));
                else
                    input2.shrink(recipe.getInputCount(1));
            }

            if (!recipe.getReturn(1).isEmpty()) {
                var returnItem = recipe.getReturn(1).copy();

                if (input2.isEmpty())
                    this.inputSlots.setItem(input2Slot, returnItem);
                else {
                    if (!recipe.isUsingDurability(1)) {
                        if (ItemHelper.canCombineStacks(input2, returnItem))
                            this.inputSlots.setItem(input2Slot, ItemHelper.combineStacks(input2, returnItem));
                        else
                            this.access.execute(((level, pos) -> Containers.dropItemStack(level, pos.getX(), pos.getY() + 1, pos.getZ(), returnItem)));
                    }
                }
            }

            this.createResult();
        } else {
            this.inputSlots.setItem(0, ItemStack.EMPTY);

            if (this.repairItemCountCost > 0) {
                ItemStack itemstack = this.inputSlots.getItem(1);

                if (!itemstack.isEmpty() && itemstack.getCount() > this.repairItemCountCost) {
                    itemstack.shrink(this.repairItemCountCost);

                    this.inputSlots.setItem(1, itemstack);
                } else
                    this.inputSlots.setItem(1, ItemStack.EMPTY);
            } else
                this.inputSlots.setItem(1, ItemStack.EMPTY);
        }
        
        this.access.execute((level, pos) -> {
            BlockState blockstate = level.getBlockState(pos);

            if (!player.getAbilities().instabuild && blockstate.is(BlockTags.ANVIL) && player.getRandom().nextFloat() < breakChance) {
                BlockState state1 = AnvilBlock.damage(blockstate);

                if (state1 == null) {
                    level.removeBlock(pos, false);
                    level.levelEvent(1029, pos, 0);
                } else {
                    level.setBlock(pos, state1, 2);
                    level.levelEvent(1030, pos, 0);
                }
            } else
                level.levelEvent(1030, pos, 0);
        });
    }

    @Inject(at = @At("HEAD"), method = "createResult", cancellable = true)
    public void createResult(CallbackInfo callback) {
        var level = this.player.level();
        var recipe = level.getRecipeManager().getRecipeFor(BARegistry.ANVIL.get(), this.inputSlots, level).orElse(null);

        if (recipe != null) {
            var stack = recipe.getResultItem(level.registryAccess());
            var output = recipe.assemble(this.inputSlots, level.registryAccess());
            var exp = recipe.getExperience();

            if (StringUtils.isBlank(this.itemName)) {
                if (stack.hasCustomHoverName()) {
                    exp++;

                    output.resetHoverName();
                }
            } else if (!this.itemName.equals(stack.getHoverName().getString())) {
                exp++;

                output.setHoverName(Component.literal(this.itemName));
            }

            this.cost.set(exp);

            this.resultSlots.setItem(0, output);
            this.broadcastChanges();

            callback.cancel();
        } else {
            var baseItem = this.inputSlots.getItem(0);
            var secondItem = this.inputSlots.getItem(1);

            if (secondItem.getItem() == Items.ENCHANTED_BOOK) {
                if (EnchantmentHelper.getEnchantments(secondItem).entrySet().stream().anyMatch(entry -> DisabledRecipes.isEnchantmentDisabled(baseItem, entry.getKey(), entry.getValue()))) {
                    callback.cancel();

                    this.resultSlots.setItem(0, ItemStack.EMPTY);
                    this.broadcastChanges();
                }
            }

            if (baseItem.getItem() == Items.ENCHANTED_BOOK) {
                if (EnchantmentHelper.getEnchantments(baseItem).entrySet().stream().anyMatch(entry -> DisabledRecipes.isEnchantmentDisabled(null, entry.getKey(), entry.getValue()))) {
                    callback.cancel();

                    this.resultSlots.setItem(0, ItemStack.EMPTY);
                    this.broadcastChanges();
                }
            }
        }
    }
}
