package ru.blatfan.blatapi.client.guide_book.recipe_renderers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.common.guide_book.GuideBookData;
import ru.blatfan.blatapi.utils.GuiUtil;

public abstract class RecipeRenderer {
    public abstract int width();
    public abstract int height();
    public abstract void render(Recipe<?> recipe, GuiGraphics gui, int x, int y, int mX, int mY, float partialTick);
    public float scale(){
        float ws = (float) GuideClient.pageWidth /width();
        float hs = (float) GuideClient.pageHeight /height();
        return Math.min(1, Math.min(ws, hs));
    }
    
    protected void renderCraftingMachine(Recipe<?> recipe, GuiGraphics gui, int x, int y){
        ItemStack stack = recipe.getToastSymbol();
        gui.renderItem(stack, x, y);
        gui.renderItemDecorations(Minecraft.getInstance().font, stack, x, y);
    }
    protected void renderSlot(GuideBookData data, GuiGraphics gui, int x, int y, int mX, int mY, ItemStack stack){
        gui.blit(data.getTexture(),x, y, 0, 68, 20, 20);
        gui.renderItem(stack, x+2, y+2);
        gui.renderItemDecorations(Minecraft.getInstance().font, stack, x+2, y+2);
        if(mX>=x && mX<=x+20 && mY>=y && mY<=y+20)
            gui.renderTooltip(GuideClient.font, stack, mX, mY);
    }
    protected void renderSlot(GuideBookData data, GuiGraphics gui, int x, int y, int mX, int mY, Ingredient stack){
        gui.blit(data.getTexture(),x, y, 0, 68, 20, 20);
        GuiUtil.renderIngredient(gui, x+2, y+2, stack);
        if(mX>=x && mX<=x+20 && mY>=y && mY<=y+20)
            GuiUtil.renderTooltip(gui, mX, mY, stack);
    }
    protected void renderResultSlot(GuideBookData data, GuiGraphics gui, int x, int y, int mX, int mY, ItemStack stack){
        gui.blit(data.getTexture(),x, y, 108, 44, 26, 26);
        gui.renderItem(stack, x+5, y+5);
        gui.renderItemDecorations(Minecraft.getInstance().font, stack, x+5, y+5);
        if(mX>=x && mX<=x+26 && mY>=y && mY<=y+26)
            gui.renderTooltip(GuideClient.font, stack, mX, mY);
    }
    protected void renderShapeless(GuideBookData data, GuiGraphics gui, int x, int y){
        gui.blit(data.getTexture(), x,y, 22, 58, 11, 11);
    }
    protected void renderArrow(GuideBookData data, GuiGraphics gui, int x, int y){
        gui.blit(data.getTexture(), x,y, 23, 51, 8, 7);
    }
    protected void renderPlus(GuideBookData data, GuiGraphics gui, int x, int y){
        gui.blit(data.getTexture(), x,y, 35, 73, 8, 8);
    }
}
