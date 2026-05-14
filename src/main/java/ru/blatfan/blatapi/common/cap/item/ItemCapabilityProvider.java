package ru.blatfan.blatapi.common.cap.item;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemCapabilityProvider implements ICapabilitySerializable<CompoundTag> {
    private final List<ICapabilitySerializable<CompoundTag>> providers;
    
    public ItemCapabilityProvider(List<ICapabilitySerializable<CompoundTag>> providers) {
        this.providers = providers;
    }
    
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        for (ICapabilitySerializable<CompoundTag> provider : providers) {
            LazyOptional<T> lazy = provider.getCapability(capability, direction);
            if(lazy.isPresent()) return lazy.cast();
        }
        return LazyOptional.empty();
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        for (int i = 0; i < providers.size(); i++) {
            ICapabilitySerializable<CompoundTag> provider = providers.get(i);
            tag.put("cap_" + i, provider.serializeNBT());
        }
        return tag;
    }
    
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        for (int i = 0; i < providers.size(); i++) {
            ICapabilitySerializable<CompoundTag> provider = providers.get(i);
            provider.deserializeNBT(nbt.getCompound("cap_" + i));
        }
    }
}
