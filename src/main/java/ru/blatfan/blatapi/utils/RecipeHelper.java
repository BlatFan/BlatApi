package ru.blatfan.blatapi.utils;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ru.blatfan.blatapi.BlatApi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    public static void remRecipe(Recipe<?> recipe) {
        recipes.remove(recipe);
    }
    public static void remRecipe(ResourceLocation id) {
        for(Recipe<?> recipe : recipes)
            if(recipe.getId()==id)
                remRecipe(recipe);
    }
    
    public static void fireRecipeManagerLoadedEvent(Map<RecipeType<?>, Object> map, ImmutableMap.Builder<ResourceLocation, Recipe<?>> builder) {
        var stopwatch = Stopwatch.createStarted();
        
        for (var recipe : recipes) {
            var recipeType = recipe.getType();
            var recipeId = recipe.getId();
            var recipeMap = map.get(recipeType);
            
            // Mohist (and I think another custom server) change it to this because annoying hacky hybrid server reasons
            if (recipeMap instanceof Object2ObjectLinkedOpenHashMap<?, ?>) {
                var o2oRecipeMap = (Object2ObjectLinkedOpenHashMap<Object, Object>) recipeMap;
                o2oRecipeMap.put(recipeId, recipe);
            } else if (recipeMap instanceof ImmutableMap.Builder<?, ?>) {
                var recipeMapBuilder = (ImmutableMap.Builder<Object, Object>) recipeMap;
                recipeMapBuilder.put(recipeId, recipe);
            } else if (recipeMap == null) {
                var recipeMapBuilder = ImmutableMap.builder();
                recipeMapBuilder.put(recipeId, recipe);
                map.put(recipeType, recipeMapBuilder);
            } else {
                BlatApi.LOGGER.error("Failed to register recipe {} to map of type {}", recipeId, recipeMap.getClass());
            }
            
            builder.put(recipeId, recipe);
        }
        
        BlatApi.LOGGER.info("Registered {} recipes in {} ms", recipes.size(), stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
    }
    
    public static void fireRecipeManagerLoadedEventKubeJSEdition(Map<ResourceLocation, Recipe<?>> recipesByName) {
        var stopwatch = Stopwatch.createStarted();
        
        for (var recipe : recipes) {
            recipesByName.put(recipe.getId(), recipe);
        }
        
        BlatApi.LOGGER.info("Registered {} recipes in {} ms (KubeJS mode)", recipes.size(), stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
    }
}