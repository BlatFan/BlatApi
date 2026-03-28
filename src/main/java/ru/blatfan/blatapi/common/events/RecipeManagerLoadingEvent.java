package ru.blatfan.blatapi.common.events;

import lombok.Getter;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

public class RecipeManagerLoadingEvent extends Event {
    @Getter
    private final RecipeManager recipeManager;
    private final List<Recipe<?>> recipes;
    
    @ApiStatus.Internal
    public RecipeManagerLoadingEvent(RecipeManager recipeManager, List<Recipe<?>> recipes) {
        this.recipeManager = recipeManager;
        this.recipes = recipes;
    }
    
    public void addRecipe(Recipe<?> recipe) {
        this.recipes.add(recipe);
    }
}