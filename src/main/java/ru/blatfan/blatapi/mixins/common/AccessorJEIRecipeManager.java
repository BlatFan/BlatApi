package ru.blatfan.blatapi.mixins.common;

import mezz.jei.library.recipes.RecipeManager;
import mezz.jei.library.recipes.RecipeManagerInternal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RecipeManager.class)
public interface AccessorJEIRecipeManager {
    @Accessor(value = "internal", remap = false)
    RecipeManagerInternal anvilapi$getInternal();
}
