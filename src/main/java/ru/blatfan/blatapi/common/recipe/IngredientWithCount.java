package ru.blatfan.blatapi.common.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import ru.blatfan.blatapi.utils.ItemHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class IngredientWithCount implements Predicate<ItemStack> {
    public final Ingredient ingredient;
    @Getter
    public final int count;
    
    public IngredientWithCount(Ingredient ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }
    
    public ItemStack getStack() {
        return new ItemStack(getItem(), getCount(), ingredient.getItems()[0].getTag());
    }
    
    public List<ItemStack> getStacks() {
        List<ItemStack> stacks = new ArrayList<>();
        for(ItemStack stack : ingredient.getItems())
            stacks.add(ItemHelper.withSize(stack, getCount(), false));
        return stacks;
    }
    
    public Item getItem() {
        return ingredient.getItems()[0].getItem();
    }
    
    @Override
    public boolean test(ItemStack stack) {
        if(stack==null || stack.isEmpty()) return false;
        return ingredient.test(stack) && stack.getCount() >= getCount();
    }
    
    public static IngredientWithCount read(FriendlyByteBuf buffer) {
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        int count = buffer.readByte();
        return new IngredientWithCount(ingredient, count);
    }
    
    public void write(FriendlyByteBuf buffer) {
        ingredient.toNetwork(buffer);
        buffer.writeByte(count);
    }
    
    public static IngredientWithCount deserialize(JsonObject object) {
        Ingredient input = object.has("ingredient_list") ? Ingredient.fromJson(object.get("ingredient_list")) : Ingredient.fromJson(object);
        int count = GsonHelper.getAsInt(object, "count", 1);
        return new IngredientWithCount(input, count);
    }
    
    public JsonObject serialize() {
        JsonObject object = new JsonObject();
        JsonElement serialize = ingredient.toJson();
        if (serialize.isJsonObject()) {
            object = serialize.getAsJsonObject();
        } else {
            object.add("ingredient_list", ingredient.toJson());
        }
        object.addProperty("count", count);
        return object;
    }
}