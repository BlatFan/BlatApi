package ru.blatfan.blatapi.common.player_stages;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerStagesProvider implements ICapabilitySerializable<Tag> {
    public static final Capability<PlayerStages> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    
    private final PlayerStages playerData = new PlayerStages();
    private final LazyOptional<PlayerStages> instance = LazyOptional.of(() -> playerData);
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == CAPABILITY ? instance.cast() : LazyOptional.empty();
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