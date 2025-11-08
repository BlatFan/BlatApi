package ru.blatfan.blatapi.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import ru.blatfan.blatapi.BlatApi;

public class IngredientWithCountSerializer implements IIngredientSerializer<IngredientWithCount> {
    public static final IngredientWithCountSerializer INSTANCE = new IngredientWithCountSerializer();
    public static final ResourceLocation ID = new ResourceLocation(BlatApi.MOD_ID, "with_count");
    
    @Override
    public IngredientWithCount parse(JsonObject json) {
        return IngredientWithCount.fromJson(json);
    }
    
    @Override
    public IngredientWithCount parse(FriendlyByteBuf buffer) {
        Ingredient base = Ingredient.fromNetwork(buffer);
        int count = buffer.readVarInt();
        return IngredientWithCount.of(base, count);
    }
    
    @Override
    public void write(FriendlyByteBuf buffer, IngredientWithCount ingredient) {
        ingredient.getBase().toNetwork(buffer);
        buffer.writeInt(ingredient.getCount());
    }
}
