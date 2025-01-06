package ru.blatfan.blatapi.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import ru.blatfan.blatapi.recipe.IRecipePattern;
import ru.blatfan.blatapi.recipe.SimpleShapedRecipe;

import java.util.Map;

public class RecipeUtils {
    public static ItemStack itemStackFromJson(JsonObject pStackObject) {
        return CraftingHelper.getItemStack(pStackObject, true, true);
    }
    
    public static Item itemFromJson(JsonObject pItemObject) {
        String s = GsonHelper.getAsString(pItemObject, "item");
        Item item = (Item) BuiltInRegistries.ITEM.getOptional(ResourceLocation.tryParse(s)).orElseThrow(() -> {
            return new JsonSyntaxException("Unknown item '" + s + "'");
        });
        if (item == Items.AIR) {
            throw new JsonSyntaxException("Empty ingredient not allowed here");
        } else {
            return item;
        }
    }
    
    public static NonNullList<Ingredient> getIngFromJson(JsonObject jsonObject){
        JsonArray jIngs = jsonObject.get("ingredients").getAsJsonArray();
        NonNullList<Ingredient> ings = NonNullList.withSize(jIngs.size(), Ingredient.of(Items.AIR));
        for(int i=0; i<ings.size(); i++)
            ings.add(i, Ingredient.fromJson(jIngs.get(i)));
        return ings;
    }
    
    public static NonNullList<Ingredient> getIngFromNetwork(FriendlyByteBuf buf){
        NonNullList<Ingredient> ings = NonNullList.withSize(buf.readInt(), Ingredient.of(Items.AIR));
        ings.replaceAll(ignored -> Ingredient.fromNetwork(buf));
        return ings;
    }
    
    public static void ingsToNetwork(FriendlyByteBuf buf, NonNullList<Ingredient> ingredients){
        buf.writeInt(ingredients.size());
        for(Object ing : ingredients.toArray())
            ((Ingredient)ing).toNetwork(buf);
    }
    
    public static NonNullList<Ingredient> patternIngsFromJson(JsonObject pJson, int width, int height){
        Map<String, Ingredient> map = IRecipePattern.keyFromJson(GsonHelper.getAsJsonObject(pJson, "key"));
        String[] astring = IRecipePattern.shrink(IRecipePattern.patternFromJson(GsonHelper.getAsJsonArray(pJson, "pattern"), width, height));
        int i = astring[0].length();
        int j = astring.length;
        return IRecipePattern.dissolvePattern(astring, map, i, j);
    }
    
    public static void patternIngsToNetwork(FriendlyByteBuf buf, SimpleShapedRecipe recipe) {
        buf.writeInt(recipe.getRecipeWidth());
        buf.writeInt(recipe.getRecipeHeight());
        for(Object ing : recipe.getIngredients().toArray())
            ((Ingredient)ing).toNetwork(buf);
    }
    public static NonNullList<Ingredient> patternIngFromNetwork(FriendlyByteBuf buf){
        int i = buf.readVarInt();
        int j = buf.readVarInt();
        
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.of(Items.AIR));
        for(int k = 0; k < nonnulllist.size(); ++k)
            nonnulllist.set(k, Ingredient.fromNetwork(buf));
        return nonnulllist;
    }
    
    public static FluidStack deserializeFluidStack(JsonObject json) {
        String fluidName = GsonHelper.getAsString(json, "fluid");
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidName));
        if (fluid == null || fluid == Fluids.EMPTY) {
            throw new JsonSyntaxException("Unknown fluid " + fluidName);
        }
        int amount = GsonHelper.getAsInt(json, "amount");
        return new FluidStack(fluid, amount);
    }
    
    public static MobEffectInstance deserializeMobEffect(JsonObject json) {
        String effectName = GsonHelper.getAsString(json, "effect");
        MobEffect mobEffect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effectName));
        if (mobEffect == null) {
            throw new JsonSyntaxException("Unknown effect " + effectName);
        }
        int duration = GsonHelper.getAsInt(json, "duration");
        int amplifier = GsonHelper.getAsInt(json, "amplifier");
        return new MobEffectInstance(mobEffect, duration, amplifier);
    }
    
    public static MobEffectInstance mobEffectFromNetwork(FriendlyByteBuf buffer) {
        if (buffer.readBoolean()) {
            MobEffect mobEffect = buffer.readRegistryId();
            int duration = buffer.readInt();
            int amplifier = buffer.readInt();
            return new MobEffectInstance(mobEffect, duration, amplifier);
        }
        return null;
    }
    
    public static void mobEffectToNetwork(MobEffectInstance effect, FriendlyByteBuf buffer) {
        if (effect == null) {
            buffer.writeBoolean(false);
        } else {
            buffer.writeBoolean(true);
            buffer.writeRegistryId(ForgeRegistries.MOB_EFFECTS, effect.getEffect());
            buffer.writeInt(effect.getDuration());
            buffer.writeInt(effect.getAmplifier());
        }
        
    }
    
    public static Enchantment deserializeEnchantment(JsonObject json) {
        String enchantmentName = GsonHelper.getAsString(json, "enchantment");
        Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(enchantmentName));
        if (enchantment == null) {
            throw new JsonSyntaxException("Unknown enchantment " + enchantmentName);
        }
        return enchantment;
    }
    
    public static Enchantment enchantmentFromNetwork(FriendlyByteBuf buffer) {
        return !buffer.readBoolean() ? null : buffer.readRegistryId();
    }
    
    public static void enchantmentToNetwork(Enchantment enchantment, FriendlyByteBuf buffer) {
        if (enchantment == null) {
            buffer.writeBoolean(false);
        } else {
            buffer.writeBoolean(true);
            buffer.writeRegistryId(ForgeRegistries.ENCHANTMENTS, enchantment);
        }
    }
}
