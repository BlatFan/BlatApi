package ru.blatfan.blatapi.common.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistryObject<T extends Block> extends RegistryObjectWrapper<T> implements ItemLike {
    BlockRegistryObject(RegistryObject<T> registryObject) {
        super(registryObject);
    }
    
    @Override
    public Item asItem() {
        return get().asItem();
    }
    public BlockState asBlockState() {
        return get().defaultBlockState();
    }
}
