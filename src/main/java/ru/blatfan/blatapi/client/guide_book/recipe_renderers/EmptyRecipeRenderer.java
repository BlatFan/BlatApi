package ru.blatfan.blatapi.client.guide_book.recipe_renderers;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.Recipe;

public class EmptyRecipeRenderer extends RecipeRenderer {
    public static EmptyRecipeRenderer INSTANCE = new EmptyRecipeRenderer();
    
    @Override
    public int width() {
        return 1;
    }
    
    @Override
    public int height() {
        return 1;
    }
    
    @Override
    public void render(Recipe<?> recipe, GuiGraphics gui, int x, int y, int mX, int mY, float partialTick) {}
}
