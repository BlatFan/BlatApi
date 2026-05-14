package ru.blatfan.blatapi.common.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistryObject<T extends Item> extends RegistryObjectWrapper<T> implements ItemLike {
    ItemRegistryObject(RegistryObject<T> registryObject) {
        super(registryObject);
    }
    
    @Override
    public Item asItem() {
        return get();
    }
}
