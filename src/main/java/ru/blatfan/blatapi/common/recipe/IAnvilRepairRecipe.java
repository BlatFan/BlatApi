package ru.blatfan.blatapi.common.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

public interface IAnvilRepairRecipe extends Recipe<Container> {
    Item getBaseItem();

    Ingredient getRepairItem();

    @Override
    default NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.of(this::getBaseItem), this.getRepairItem());
    }
}
