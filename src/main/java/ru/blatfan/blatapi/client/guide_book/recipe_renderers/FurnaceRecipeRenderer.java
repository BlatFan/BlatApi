package ru.blatfan.blatapi.client.guide_book.recipe_renderers;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.*;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.common.guide_book.GuideBookData;

public class FurnaceRecipeRenderer extends RecipeRenderer {
    protected FurnaceRecipeRenderer(){}
    public static final FurnaceRecipeRenderer INSTANCE = new FurnaceRecipeRenderer();
    
    @Override
    public int width() {
        return 56;
    }
    
    @Override
    public int height() {
        return 33;
    }
    
    @Override
    public void render(Recipe<?> recipe, GuiGraphics gui, int x, int y, int mX, int mY, float partialTick) {
        GuideBookData data = GuideClient.guideBookData;
        if(data==null) return;
        if(recipe instanceof AbstractCookingRecipe craft){
            renderSlot(data, gui, x, y+3, mX, mY, craft.getIngredients().get(0));
            renderArrow(data, gui, x+23, y+9);
            renderResultSlot(data, gui, x+33, y, mX, mY, craft.getResultItem(null));
            renderCraftingMachine(recipe, gui, x+23, y+17);
        }
    }
}
