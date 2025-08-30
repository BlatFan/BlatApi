package ru.blatfan.blatapi.utils;

import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.registries.ForgeRegistries;
import ru.blatfan.blatapi.BlatApi;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemHelper {
    public static JsonObject serialize(Item item) {
        var json = new JsonObject();

        json.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).toString());

        return json;
    }

    public static JsonObject serialize(ItemStack stack) {
        var json = new JsonObject();

        json.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stack.getItem())).toString());

        if (stack.getCount() > 1)
            json.addProperty("count", stack.getCount());

        if (stack.hasTag())
            json.addProperty("nbt", Objects.requireNonNull(stack.getTag()).toString());

        return json;
    }

    public static ItemStack withSize(ItemStack stack, int size, boolean container) {
        if (size <= 0) {
            if (container && stack.hasCraftingRemainingItem())
                return stack.getCraftingRemainingItem();
            else
                return ItemStack.EMPTY;
        }

        stack = stack.copy();
        stack.setCount(size);

        return stack;
    }
    
    public static ItemStack grow(ItemStack stack, int amount) {
        return withSize(stack, stack.getCount() + amount, false);
    }
    
    public static ItemStack shrink(ItemStack stack) {
        return withSize(stack, stack.getCount() - 1, false);
    }

    public static boolean areItemsEqual(ItemStack stack1, ItemStack stack2) {
        return !stack1.isEmpty() && !stack2.isEmpty() && stack1.is(stack2.getItem());
    }

    public static boolean areStacksEqual(ItemStack stack1, ItemStack stack2) {
        return areItemsEqual(stack1, stack2) && ItemStack.isSameItemSameTags(stack1, stack2);
    }

    public static boolean canCombineStacks(ItemStack stack1, ItemStack stack2) {
        if (!stack1.isEmpty() && stack2.isEmpty())
            return true;

        return areStacksEqual(stack1, stack2) && (stack1.getCount() + stack2.getCount()) <= stack1.getMaxStackSize();
    }

    public static ItemStack combineStacks(ItemStack stack1, ItemStack stack2) {
        if (stack1.isEmpty())
            return stack2.copy();

        return grow(stack1, stack2.getCount());
    }
    
    public static void registerCompostable(float chance, ItemLike item) {
        synchronized (ComposterBlock.COMPOSTABLES) {
            try {
                ComposterBlock.COMPOSTABLES.put(item, chance);
            } catch (Exception ex) {
                BlatApi.LOGGER.error("Failed to register compostable item: {}, chance {}", ForgeRegistries.ITEMS.getKey(item.asItem()), chance);
            }
        }
    }
}
