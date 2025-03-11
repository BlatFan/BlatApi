package ru.blatfan.blatapi.common.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public abstract class SimpleShapedRecipe<T extends CraftingContainer> extends SimpleRecipe<T> implements IRecipePattern {
    private final int width, height;
    
    public SimpleShapedRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack result,
                              int width, int height) {
        super(id, ingredients, result);
        this.width = width;
        this.height = height;
    }
    
    @Override
    public int getRecipeWidth() {
        return width;
    }
    
    @Override
    public int getRecipeHeight() {
        return height;
    }
    
    @Override
    public boolean matches(T pInv, Level pLevel) {
        for(int i = 0; i <= pInv.getWidth() - this.width; ++i)
            for(int j = 0; j <= pInv.getHeight() - this.height; ++j) {
                if (this.matches(pInv, i, j, true))
                    return true;
                if (this.matches(pInv, i, j, false))
                    return true;
            }
        return false;
    }
    
    private boolean matches(T pCraftingInventory, int pWidth, int pHeight, boolean pMirrored) {
        for(int i = 0; i < pCraftingInventory.getWidth(); ++i)
            for(int j = 0; j < pCraftingInventory.getHeight(); ++j) {
                int k = i - pWidth;
                int l = j - pHeight;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.width && l < this.height)
                    if (pMirrored)
                        ingredient = this.getIngredients().get(this.width - k - 1 + l * this.width);
                    else
                        ingredient = this.getIngredients().get(k + l * this.width);
                
                if (!ingredient.test(pCraftingInventory.getItem(i + j * pCraftingInventory.getWidth())))
                    return false;
            }
        return true;
    }
}
