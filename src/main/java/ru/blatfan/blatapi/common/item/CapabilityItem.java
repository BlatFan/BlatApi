package ru.blatfan.blatapi.common.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import org.jetbrains.annotations.Nullable;
import ru.blatfan.blatapi.common.cap.item.ItemCapabilityProvider;

import java.util.ArrayList;
import java.util.List;

public abstract class CapabilityItem extends Item {
    public CapabilityItem(Item.Properties properties) {
        super(properties);
    }
    
    public abstract void gatherCapability(List<ICapabilitySerializable<CompoundTag>> providers, ItemStack stack);
    
    @Override
    public final ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        List<ICapabilitySerializable<CompoundTag>> providers = new ArrayList<>();
        gatherCapability(providers, stack);
        return new ItemCapabilityProvider(providers);
    }
}