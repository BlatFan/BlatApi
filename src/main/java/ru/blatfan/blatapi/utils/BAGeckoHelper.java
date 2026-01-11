package ru.blatfan.blatapi.utils;

import lombok.experimental.UtilityClass;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import software.bernie.geckolib.animatable.GeoItem;

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
    }
}