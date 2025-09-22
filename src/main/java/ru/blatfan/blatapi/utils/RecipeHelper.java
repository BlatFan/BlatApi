package ru.blatfan.blatapi.utils;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.blatfan.blatapi.common.events.RecipeManagerLoadingEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class RecipeHelper {
    private static RecipeManager recipeManager;
    private static final Logger LOGGER = LogManager.getLogger("BA | Recipe Helper");
    
    public static RecipeManager getRecipeManager() {
        if(recipeManager==null) return new RecipeManager(ICondition.IContext.EMPTY);
        return recipeManager;
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onAddReloadListeners(AddReloadListenerEvent event) {
        recipeManager = event.getServerResources().getRecipeManager();
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRecipesUpdated(RecipesUpdatedEvent event) {
        recipeManager = event.getRecipeManager();
    }
    
    public static Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> getRecipes() {
        return getRecipeManager().recipes;
    }
    
    public static Recipe<?> getRecipe(ResourceLocation id){
        for(Recipe<?> recipe : getRecipeManager().getRecipes())
            if(recipe.getId().equals(id)) return recipe;
        return null;
    }
    
    public static <C extends Container, T extends Recipe<C>> List<T> getRecipes(RecipeType<T> type) {
        return getRecipeManager().getAllRecipesFor(type);
    }
    
    public static void addRecipe(Recipe<?> recipe) {
        if (recipeManager.recipes instanceof ImmutableMap) {
            recipeManager.recipes = new HashMap<>(recipeManager.recipes);
            recipeManager.recipes.replaceAll((t, v) -> new HashMap<>(recipeManager.recipes.get(t)));
        }
        
        if (recipeManager.byName instanceof ImmutableMap) {
            recipeManager.byName = new HashMap<>(recipeManager.byName);
        }
        
        getRecipeManager().recipes.computeIfAbsent(recipe.getType(), t -> new HashMap<>()).put(recipe.getId(), recipe);
        getRecipeManager().byName.put(recipe.getId(), recipe);
    }
    
    public static void fireRecipeManagerLoadedEvent(RecipeManager manager, Map<RecipeType<?>, Object> map, ImmutableMap.Builder<ResourceLocation, Recipe<?>> builder) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        List<Recipe<?>> recipes = new ArrayList<>();
        
        try {
            MinecraftForge.EVENT_BUS.post(new RecipeManagerLoadingEvent(manager, recipes));
        } catch (Exception e) {
            LOGGER.error("An error occurred while firing RecipeManagerLoadingEvent", e);
        }
        
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
                LOGGER.error("Failed to register recipe {} to map of type {}", recipeId, recipeMap.getClass());
            }
            
            builder.put(recipeId, recipe);
        }
        
        LOGGER.info("Registered {} recipes in {} ms", recipes.size(), stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
    }
    
    public static void fireRecipeManagerLoadedEventKubeJSEdition(RecipeManager manager, Map<ResourceLocation, Recipe<?>> recipesByName) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        List<Recipe<?>> recipes = new ArrayList<>();
        
        try {
            MinecraftForge.EVENT_BUS.post(new RecipeManagerLoadingEvent(manager, recipes));
        } catch (Exception e) {
            LOGGER.error("An error occurred while firing RecipeManagerLoadingEvent", e);
        }
        
        for (var recipe : recipes) {
            recipesByName.put(recipe.getId(), recipe);
        }
        
        LOGGER.info("Registered {} recipes in {} ms (KubeJS mode)", recipes.size(), stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
    }
}