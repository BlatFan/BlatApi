package ru.blatfan.blatapi.common.guide_book.pages;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.client.guide_book.recipe_renderers.EmptyRecipeRenderer;
import ru.blatfan.blatapi.client.guide_book.recipe_renderers.RecipeRenderer;
import ru.blatfan.blatapi.common.guide_book.GuideBookPage;
import ru.blatfan.blatapi.utils.ColorHelper;
import ru.blatfan.blatapi.utils.RecipeHelper;
import ru.blatfan.blatapi.utils.collection.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipePage extends GuideBookPage {
    public static final ResourceLocation TYPE = BlatApi.loc("recipe");
    private static final Map<ResourceLocation, RecipeRenderer> TYPES = new HashMap<>();
    
    public static void addRecipeType(ResourceLocation recipeType, RecipeRenderer recipeRenderer){
        if(!TYPES.containsKey(recipeType))
            TYPES.put(recipeType, recipeRenderer);
    }
    
    private final List<ResourceLocation> recipeIds;
    private final int height;
    
    public RecipePage(List<ResourceLocation> recipeIds, boolean separator, Component title, Color titleColor) {
        super(title, titleColor, separator);
        this.recipeIds = recipeIds;
        
        int h = 11;
        for(int i=0; i<recipeIds.size(); i++){
            RecipeRenderer rr = getRecipeRenderer(getRecipeType(i));
            if(rr==null) rr = EmptyRecipeRenderer.INSTANCE;
            h+= (int) (rr.height()/rr.scale())+2;
        }
        this.height=h;
    }
    
    protected static RecipeRenderer getRecipeRenderer(ResourceLocation type){
        if(TYPES.containsKey(type)) return TYPES.get(type);
        return EmptyRecipeRenderer.INSTANCE;
    }
    
    public Recipe<?> getRecipe(int i){
        return RecipeHelper.getRecipe(recipeIds.get(i));
    }
    public ResourceLocation getRecipeType(int i){
        if(getRecipe(i)==null) return new ResourceLocation("null");
        return ResourceLocation.tryParse(getRecipe(i).getType().toString());
    }
    
    public static GuideBookPage json(JsonObject jsonObject) {
        List<ResourceLocation> ids = new ArrayList<>();
        for(JsonElement el : jsonObject.get("recipes").getAsJsonArray())
            ids.add(ResourceLocation.tryParse(el.getAsString()));
        return new RecipePage(
            ids,
            jsonObject.has("separator") && jsonObject.get("separator").getAsBoolean(),
            Text.create(jsonObject.get("title").getAsString()),
            jsonObject.has("title_color") ? ColorHelper.getColor(jsonObject.get("title_color").getAsString()) : Color.WHITE
        );
    }
    
    @Override
    public int height() {
        return height;
    }
    
    @Override
    public void render(GuiGraphics gui, int x, int y, int mX, int mY, float partialTick) {
        int h = 0;
        for(int i=0; i<recipeIds.size(); i++){
            RecipeRenderer rr = getRecipeRenderer(getRecipeType(i));
            if(rr==null) rr = EmptyRecipeRenderer.INSTANCE;
            gui.pose().pushPose();
            gui.pose().scale(rr.scale(), rr.scale(), 1);
            rr.render(getRecipe(i), gui, (int)((x+(GuideClient.pageWidth/2f)-(rr.width()/2f))*rr.scale()), (int)((y+h)/rr.scale()),
                mX, mY, partialTick);
            gui.pose().popPose();
            h+= (int) (rr.height()/rr.scale())+2;
            gui.setColor(1, 1, 1, 0.7f);
            gui.blitNineSlicedSized(GuideClient.guideBookData.getTexture(), x+8, y+h-4, GuideClient.pageWidth-10, 4,
                8, 24, 4, 144, 32, 256, 256);
            gui.setColor(1, 1, 1, 1);
        }
    }
}