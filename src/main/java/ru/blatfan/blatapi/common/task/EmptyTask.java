package ru.blatfan.blatapi.common.task;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class EmptyTask extends Task{
    @Override
    public boolean get(Player player) {
        return true;
    }
    
    @Override
    public boolean isVisible() {
        return false;
    }
    
    @Override
    public Component text(Player player) {
        return null;
    }
}
