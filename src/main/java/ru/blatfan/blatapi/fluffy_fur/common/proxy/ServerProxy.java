package ru.blatfan.blatapi.fluffy_fur.common.proxy;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ServerProxy implements ISidedProxy {
    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public Level getLevel() {
        return null;
    }
}
