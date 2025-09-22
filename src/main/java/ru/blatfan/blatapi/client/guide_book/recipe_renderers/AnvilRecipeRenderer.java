package ru.blatfan.blatapi.client.guide_book.recipe_renderers;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.Recipe;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.common.guide_book.GuideBookData;
import ru.blatfan.blatapi.common.recipe.AnvilRecipe;

public class AnvilRecipeRenderer extends RecipeRenderer {
    protected AnvilRecipeRenderer(){}
    public static final AnvilRecipeRenderer INSTANCE = new AnvilRecipeRenderer();
    
    @Override
    public int width() {
        return 88;
    }
    
    @Override
    public int height() {
        return 31;
    }
    
    @Override
    public void render(Recipe<?> recipe, GuiGraphics gui, int x, int y, int mX, int mY, float partialTick) {
        GuideBookData data = GuideClient.guideBookData;
        if(data==null) return;
        if(recipe instanceof AnvilRecipe craft){
            renderSlot(data, gui, x, y+2, mX, mY, craft.getIngredients().get(0));
            renderPlus(data, gui, x+21, y+8);
            renderSlot(data, gui, x+32, y+2, mX, mY, craft.getIngredients().get(1));
            renderArrow(data, gui, x+54, y+8);
            renderResultSlot(data, gui, x+62, y, mX, mY, craft.getResultItem(null));
            renderCraftingMachine(recipe, gui, x+58, y+15);
            if(craft.isShapeless()) renderShapeless(data, gui, x + 54, y + 15);
        }
    }
}
