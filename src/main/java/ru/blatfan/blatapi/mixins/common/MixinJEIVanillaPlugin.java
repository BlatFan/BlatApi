package ru.blatfan.blatapi.mixins.common;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.library.plugins.vanilla.VanillaPlugin;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(VanillaPlugin.class)
public abstract class MixinJEIVanillaPlugin implements IModPlugin {
    @Redirect(method = "registerGuiHandlers", at = @At(value = "INVOKE", target = "Lmezz/jei/api/registration/IGuiHandlerRegistration;addRecipeClickArea(Ljava/lang/Class;IIII[Lmezz/jei/api/recipe/RecipeType;)V"), remap = false)
    private <T extends AbstractContainerScreen<?>> void anvilapi$addRecipeClickArea(IGuiHandlerRegistration registration, Class<? extends T> containerScreenClass, int xPos, int yPos, int width, int height, RecipeType<?>... recipeTypes) {
        if (recipeTypes[0] != RecipeTypes.ANVIL)
            registration.addRecipeClickArea(containerScreenClass, xPos, yPos, width, height, recipeTypes);
    }
}
