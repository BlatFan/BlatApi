package ru.blatfan.blatapi.common.reward;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class CloseMenuReward extends ClientReward{
    @Override
    public void apply(Player player) {
        Minecraft.getInstance().setScreen(null);
    }
    
    @Override
    public boolean isVisible() {
        return false;
    }
    
    @Override
    public CompoundTag toTag() {
        return new CompoundTag();
    }
    
    public static Reward fromTag(CompoundTag tag) {
        return new CloseMenuReward();
    }
    
    @Override
    public Component text(Player player) {
        return Component.empty();
    }
}
