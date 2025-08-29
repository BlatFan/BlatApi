package ru.blatfan.blatapi.common.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;

public interface IAnvilRecipe extends Recipe<Container> {
    @Override
    NonNullList<Ingredient> getIngredients();

    Ingredient getInput(int inputId);

    NonNullList<ItemStack> getReturns();

    ItemStack getReturn(int inputId);

    List<Integer> getCounts();

    int getInputCount(int inputId);

    List<Boolean> getConsumes();

    boolean isConsuming(int inputId);

    List<Boolean> getUseDurability();

    boolean isUsingDurability(int inputId);

    List<CompoundTag> getAllNbt();

    CompoundTag getInputNbt(int inputId);

    boolean hasNbt(int inputId);

    List<Boolean> getStrictNbt();

    boolean isNbtStrict(int inputId);

    boolean isShapeless();

    int getExperience();
}
