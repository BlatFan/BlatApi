package ru.blatfan.blatapi.fluffy_fur.client.playerskin;

import lombok.Getter;
import net.minecraft.world.entity.player.Player;

import java.util.Random;

@Getter
public class PlayerSkinEffect {
    public String id;

    public static Random random = new Random();

    public PlayerSkinEffect(String id) {
        this.id = id;
    }
    
    public void tick(Player player) {

    }
}
