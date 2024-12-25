package ru.blatfan.blatapi.utils;

import lombok.Getter;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class RecipeHelper {
    private static RecipeManager MANAGER;
    @Getter
    private static Collection<Recipe<?>> recipes = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onAddReloadListeners(AddReloadListenerEvent event) {
        MANAGER = event.getServerResources().getRecipeManager();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRecipesUpdated(RecipesUpdatedEvent event) {
        MANAGER = event.getRecipeManager();
    }

    public static RecipeManager getRecipeManager() {
        for(Recipe<?> recipe : MANAGER.getRecipes()){
            if(!recipes.contains(recipe))
                addRecipe(recipe);
        }
        MANAGER.replaceRecipes(recipes);

        return MANAGER;
    }
    
    public static <T extends RecipeType<?>> List<Recipe<?>> getRecipes(T type) {
        List<Recipe<?>> list = new ArrayList<>();
        for(Recipe<?> recipe : recipes)
            if(recipe.getType()==type)
                list.add(recipe);
        return list;
    }

    public static void addRecipe(Recipe<?> recipe) {
        recipes.add(recipe);
    }
}