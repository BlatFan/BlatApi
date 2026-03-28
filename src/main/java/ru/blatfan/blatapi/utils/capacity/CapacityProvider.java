package ru.blatfan.blatapi.utils.capacity;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import java.util.function.Supplier;

public class CapacityProvider<C extends ICapacity<C>> implements ICapabilitySerializable<Tag> {
    private final Capability<C> capability;
    
    private final C playerData;
    private final LazyOptional<C> instance;
    
    public CapacityProvider(Capability<C> capability, Supplier<C> defaultInstance) {
        this.capability = capability;
        this.playerData = defaultInstance.get();
        this.instance = LazyOptional.of(() -> playerData);
    }
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == capability ? instance.cast() : LazyOptional.empty();
    }
    
    @Override
    public Tag serializeNBT() {
        return playerData.toNBT();
    }
    
    @Override
    public void deserializeNBT(Tag nbt) {
        playerData.fromNBT(nbt);
    }
}