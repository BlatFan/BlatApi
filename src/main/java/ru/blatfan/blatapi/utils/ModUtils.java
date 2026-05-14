package ru.blatfan.blatapi.utils;

import lombok.experimental.UtilityClass;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fml.ModList;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@UtilityClass
public class ModUtils {
    private static final Map<String,String> modId2Name = new HashMap<>();
    
    public static Optional<String> getModName(String modId) {
        if (modId2Name.isEmpty()) {
            ModList.get().getMods().forEach(mod -> modId2Name.put(mod.getModId(), mod.getDisplayName()));
        }
        
        return Optional.ofNullable(modId2Name.get(modId));
    }
    
    public static Optional<String> getModName(ItemStack item) {
        Optional<String> creator = getModName(item.getItem().getCreatorModId(item));
        return creator.isEmpty() ? getModName(item.getItem()) : creator;
    }
    
    public static Optional<String> getModName(Item item) {
        return getModName(BuiltInRegistries.ITEM.getKey(item).getNamespace());
    }
    
    public static Optional<String> getModName(Block block) {
        return getModName(BuiltInRegistries.BLOCK.getKey(block).getNamespace());
    }
    
    public static Optional<String> getModName(Fluid fluid) {
        return getModName(BuiltInRegistries.FLUID.getKey(fluid).getNamespace());
    }
    
    public static Optional<String> getModName(EntityType<?> entity) {
        return getModName(BuiltInRegistries.ENTITY_TYPE.getKey(entity).getNamespace());
    }
}