package ru.blatfan.blatapi.fluffy_fur.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

public class IntegrationUtil {
    public static Item getItem(String modId, String id) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(modId, id));
        return item != null ? item : Items.DIRT;
    }
}
