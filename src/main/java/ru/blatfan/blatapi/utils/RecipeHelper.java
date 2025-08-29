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
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.blatfan.blatapi.BlatApi;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Mod.EventBusSubscriber(modid = BlatApi.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class RecipeHelper {
    private static final Logger LOGGER = LogManager.getLogger("BA | Recipe Helper");
    private static RecipeManager MANAGER;
    private static final Collection<Recipe<?>> recipes = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private static boolean initialized = false;
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        MANAGER = event.getServerResources().getRecipeManager();
        initialized = true;
        LOGGER.info("RecipeManager initialized with {} recipes", MANAGER.getRecipes().size());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRecipesUpdated(RecipesUpdatedEvent event) {
        MANAGER = event.getRecipeManager();
        initialized = true;
        LOGGER.info("Recipes updated, now contains {} recipes", MANAGER.getRecipes().size());
    }
    
    public static RecipeManager getRecipeManager() {
        if (!initialized || MANAGER == null) {
            LOGGER.warn("RecipeManager accessed before initialization, returning empty manager");
            return createFallbackManager();
        }
        return MANAGER;
    }
    
    private static RecipeManager createFallbackManager() {
        return new RecipeManager();
    }
    
    public static <C extends Container, T extends Recipe<C>> List<T> getRecipes(RecipeType<T> type) {
        if (!initialized || MANAGER == null) {
            LOGGER.warn("RecipeManager not initialized, returning empty list");
            return Collections.emptyList();
        }
        return List.copyOf(MANAGER.getAllRecipesFor(type));
    }
    
    public static void addRecipe(Recipe<?> recipe) {
        if (recipe != null) {
            recipes.add(recipe);
            LOGGER.debug("Added custom recipe: {}", recipe.getId());
        }
    }
    
    public static void remRecipe(Recipe<?> recipe) {
        if (recipe != null) {
            recipes.remove(recipe);
            LOGGER.debug("Removed custom recipe: {}", recipe.getId());
        }
    }
    
    public static void remRecipe(ResourceLocation id) {
        if (id != null) {
            recipes.removeIf(recipe -> id.equals(recipe.getId()));
            LOGGER.debug("Removed custom recipe by ID: {}", id);
        }
    }
    
    public static void fireRecipeManagerLoadedEvent(Map<RecipeType<?>, Object> map, ImmutableMap.Builder<ResourceLocation, Recipe<?>> builder) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        int count = 0;
        
        for (Recipe<?> recipe : recipes) {
            try {
                RecipeType<?> recipeType = recipe.getType();
                ResourceLocation recipeId = recipe.getId();
                
                Object recipeMap = map.get(recipeType);
                if (recipeMap instanceof Object2ObjectLinkedOpenHashMap) {
                    ((Object2ObjectLinkedOpenHashMap<ResourceLocation, Recipe<?>>) recipeMap).put(recipeId, recipe);
                    count++;
                } else if (recipeMap instanceof ImmutableMap.Builder) {
                    ((ImmutableMap.Builder<ResourceLocation, Recipe<?>>) recipeMap).put(recipeId, recipe);
                    count++;
                } else if (recipeMap == null) {
                    Object2ObjectLinkedOpenHashMap<ResourceLocation, Recipe<?>> newMap = new Object2ObjectLinkedOpenHashMap<>();
                    newMap.put(recipeId, recipe);
                    map.put(recipeType, newMap);
                    count++;
                } else {
                    LOGGER.error("Failed to register recipe {} to map of type {}", recipeId, recipeMap.getClass());
                }
                
                builder.put(recipeId, recipe);
            } catch (Exception e) {
                LOGGER.error("Error registering custom recipe {}: {}", recipe.getId(), e.getMessage());
            }
        }
        
        LOGGER.info("Registered {} custom recipes in {} ms", count, stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
    }
    
    public static void fireRecipeManagerLoadedEventKubeJSEdition(Map<ResourceLocation, Recipe<?>> recipesByName) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        int count = 0;
        
        for (Recipe<?> recipe : recipes) {
            try {
                recipesByName.put(recipe.getId(), recipe);
                count++;
            } catch (Exception e) {
                LOGGER.error("Error registering custom recipe {} in KubeJS mode: {}", recipe.getId(), e.getMessage());
            }
        }
        
        LOGGER.info("Registered {} custom recipes in {} ms (KubeJS mode)", count, stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
    }
    
    public static Collection<Recipe<?>> getCustomRecipes() {
        return Collections.unmodifiableCollection(recipes);
    }
    
    public static void clearCustomRecipes() {
        recipes.clear();
        LOGGER.info("Cleared all custom recipes");
    }
}