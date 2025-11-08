package ru.blatfan.blatapi.common.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.stream.Stream;

@Getter
public class IngredientWithCount extends Ingredient {
    private final Ingredient base;
    private final int count;
    
    private IngredientWithCount(Ingredient base, int count) {
        super(Stream.of(base.values));
        this.base = base;
        this.count = Math.max(1, count);
    }
    
    public static IngredientWithCount of(Ingredient base, int count){
        return new IngredientWithCount(base, count);
    }
    public static IngredientWithCount of(Ingredient base){
        return new IngredientWithCount(base, 1);
    }
    
    @Override
    public boolean test(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return base.test(stack) && stack.getCount() >= count;
    }
    
    @Override
    public ItemStack[] getItems() {
        ItemStack[] baseItems = base.getItems();
        ItemStack[] items = new ItemStack[baseItems.length];
        for (int i = 0; i < baseItems.length; i++) {
            items[i] = baseItems[i].copy();
            items[i].setCount(count);
        }
        return items;
    }
    
    @Override
    public boolean isSimple() {
        return base.isSimple();
    }
    
    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.add("ingredient", base.toJson());
        if (count > 1) {
            json.addProperty("count", count);
        }
        return json;
    }
    
    public static IngredientWithCount fromJson(JsonElement element) {
        if (element.isJsonObject()) {
            JsonObject json = element.getAsJsonObject();
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            int count = json.has("count") ? json.get("count").getAsInt() : 1;
            return new IngredientWithCount(ingredient, count);
        }
        return new IngredientWithCount(Ingredient.fromJson(element), 1);
    }
    
    public String toString() {
        return "IngredientWithCount(base=" + this.getBase().toString() + ", count=" + this.getCount() + ")";
    }
}