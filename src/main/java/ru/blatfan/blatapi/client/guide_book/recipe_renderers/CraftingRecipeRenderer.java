package ru.blatfan.blatapi.client.guide_book.recipe_renderers;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.*;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.common.guide_book.GuideBookData;

public class CraftingRecipeRenderer extends RecipeRenderer {
    protected CraftingRecipeRenderer(){}
    public static final CraftingRecipeRenderer INSTANCE = new CraftingRecipeRenderer();
    
    @Override
    public int width() {
        return 92;
    }
    
    @Override
    public int height() {
        return 62;
    }
    
    @Override
    public void render(Recipe<?> recipe, GuiGraphics gui, int x1, int y1, int mX, int mY, float partialTick) {
        GuideBookData data = GuideClient.guideBookData;
        if(data==null) return;
        if(recipe instanceof ShapelessRecipe craft){
            for(int i=0; i<craft.getIngredients().size(); i++) {
                Ingredient ingredient = craft.getIngredients().get(i);
                int x = i % 3 * 20;
                int y = i / 3 * 20;
                renderSlot(data, gui, x1+x, y1+y, mX, mY, ingredient);
            }
            renderArrow(data, gui, x1+62, y1+21);
            renderShapeless(data, gui, x1+56, y1+31);
            renderResultSlot(data, gui, x1+70, y1+17, mX, mY, craft.getResultItem(null));
            renderCraftingMachine(recipe, gui, x1+62, y1+29);
        }
        if(recipe instanceof ShapedRecipe craft){
            for(int i = 0; i <= 3 - craft.getWidth(); ++i)
                for(int j = 0; j <= 3 - craft.getHeight(); ++j)
                    shaped(craft, gui, i, j, x1, y1, mX, mY);
            renderArrow(data, gui, x1+62, y1+21);
            renderResultSlot(data, gui, x1+70, y1+17, mX, mY, craft.getResultItem(null));
            renderCraftingMachine(recipe, gui, x1+62, y1+29);
        }
    }
    private void shaped(ShapedRecipe craft, GuiGraphics gui, int pWidth, int pHeight, int x1, int y1, int mX, int mY){
        GuideBookData data = GuideClient.guideBookData;
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                int k = i - pWidth;
                int l = j - pHeight;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < craft.getWidth() && l < craft.getHeight()) {
                    ingredient = craft.getIngredients().get(k + l * craft.getWidth());
                }
                renderSlot(data, gui, x1 + (Math.max(0, Math.min(3, k)) * 20), y1 + (Math.max(0, Math.min(3, l)) * 20), mX, mY, ingredient);
            }
        }
    }
}
