package ru.blatfan.blatapi.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.RecipesEventJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import ru.blatfan.blatapi.utils.RecipeHelper;

import java.util.Map;

public class BAKubeJS extends KubeJSPlugin {
    @Override
    public void injectRuntimeRecipes(RecipesEventJS event, RecipeManager manager, Map<ResourceLocation, Recipe<?>> recipesByName) {
        RecipeHelper.fireRecipeManagerLoadedEventKubeJSEdition(manager, recipesByName);
    }
}