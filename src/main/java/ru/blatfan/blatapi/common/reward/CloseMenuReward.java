package ru.blatfan.blatapi.common.reward;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.utils.collection.Text;

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
    public Text text(Player player) {
        return Text.create();
    }
}
