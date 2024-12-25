package ru.blatfan.blatapi.fluffy_fur.client.playerskin;

import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class PlayerSkinCape {
    @Getter
    public String id;

    public ResourceLocation texture;

    public PlayerSkinCape(String id) {
        this.id = id;
    }

    public PlayerSkinCape setTexture(ResourceLocation texture) {
        this.texture = texture;
        return this;
    }

    public ResourceLocation getSkin(Player player) {
        return texture;
    }
    
    public static ResourceLocation getCapeLocation(String mod, String texture) {
        return new ResourceLocation(mod, "textures/entity/cape/" + texture + ".png");
    }
}
