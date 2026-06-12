package ru.blatfan.blatapi.utils;

import lombok.experimental.UtilityClass;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.ModList;
import software.bernie.example.registry.ItemRegistry;
import software.bernie.geckolib.animatable.GeoItem;

import java.util.Arrays;
import java.util.List;

@UtilityClass
public class BAGeckoHelper {
    public static boolean isLoaded(){
        return ModList.get().isLoaded("geckolib");
    }
    
    public static boolean isGeckoArmor(ItemStack stack){
        if(isLoaded()) return GeckoLibHelper.isGeckoArmor(stack);
        return false;
    }
    public static int getGeckoArmorOffset(float sizeY, EquipmentSlot slot){
        if(isLoaded()) return GeckoLibHelper.getGeckoArmorOffset(sizeY, slot);
        return Integer.MAX_VALUE;
    }
    
    public static List<Item> getList() {
        if(isLoaded())
            return GeckoLibHelper.getList();
        return Arrays.asList(
            Items.DIAMOND_HELMET,
            Items.DIAMOND_CHESTPLATE,
            Items.DIAMOND_LEGGINGS,
            Items.DIAMOND_BOOTS
        );
    }
    
    private class GeckoLibHelper {
        private static boolean isGeckoArmor(ItemStack stack){
            return stack.getItem() instanceof ArmorItem && stack.getItem() instanceof GeoItem;
        }
        
        private static int getGeckoArmorOffset(float sizeY, EquipmentSlot slot){
            if(slot.isArmor()) return (int) (sizeY/16) + (int) ((sizeY/16)* switch (slot){
                case CHEST -> 6.6;
                case LEGS -> 10.6;
                case FEET -> 12.6;
                default -> 0;
            });
            return 0;
        }
        
        public static List<Item> getList() {
            return Arrays.asList(
                Items.DIAMOND_HELMET,
                ItemRegistry.WOLF_ARMOR_HELMET.get(),
                ItemRegistry.GECKO_ARMOR_HELMET.get(),
                Items.DIAMOND_CHESTPLATE,
                ItemRegistry.WOLF_ARMOR_CHESTPLATE.get(),
                ItemRegistry.GECKO_ARMOR_CHESTPLATE.get(),
                Items.DIAMOND_LEGGINGS,
                ItemRegistry.WOLF_ARMOR_LEGGINGS.get(),
                ItemRegistry.GECKO_ARMOR_LEGGINGS.get(),
                Items.DIAMOND_BOOTS,
                ItemRegistry.WOLF_ARMOR_BOOTS.get(),
                ItemRegistry.GECKO_ARMOR_BOOTS.get()
            );
        }
    }
}