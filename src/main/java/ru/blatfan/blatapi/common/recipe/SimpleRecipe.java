package ru.blatfan.blatapi.common.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public abstract class SimpleRecipe<T extends Container> implements Recipe<T> {
    protected final ResourceLocation id;
    protected final NonNullList<Ingredient> ingredients;
    protected final ItemStack result;
    
    public SimpleRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack result) {
        this.id = id;
        this.ingredients = ingredients;
        this.result = result;
    }
    
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }
    
    @Override
    public boolean isSpecial() {
        return true;
    }
    
    public abstract ItemStack getToastSymbol();
    
    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return result.copy();
    }
    
    public abstract boolean matches(T t, Level level);
    
    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }
    
    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return result.copy();
    }
    
    @Override
    public ResourceLocation getId() {
        return id;
    }
    
    public abstract RecipeSerializer<?> getSerializer();
    
    public abstract RecipeType<?> getType();
}
