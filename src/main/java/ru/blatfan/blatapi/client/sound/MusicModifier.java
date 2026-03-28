package ru.blatfan.blatapi.client.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import ru.blatfan.blatapi.client.gui.screen.BAModsHandler;
import ru.blatfan.blatapi.client.gui.screen.BAPanorama;

public class MusicModifier {

    public boolean isCanPlay(Music defaultMisic, Minecraft minecraft) {
        return true;
    }

    public Music play(Music defaultMisic, Minecraft minecraft) {
        return null;
    }

    public boolean isMenu(Music defaultMisic, Minecraft minecraft) {
        return false;
    }

    public boolean isBiome(Biome biome, Minecraft minecraft) {
        if (minecraft.player != null) {
            Holder<Biome> holder = minecraft.player.level().getBiome(minecraft.player.blockPosition());
            return (holder.get().equals(biome));
        }
        return false;
    }

    public boolean isBiome(TagKey<Biome> biome, Minecraft minecraft) {
        if (minecraft.player != null) {
            Holder<Biome> holder = minecraft.player.level().getBiome(minecraft.player.blockPosition());
            return (holder.is(biome));
        }
        return false;
    }

    public static class Panorama extends MusicModifier {
        public Music music;
        public BAPanorama panorama;

        public Panorama(Music music, BAPanorama panorama) {
            this.music = music;
            this.panorama = panorama;
        }

        @Override
        public boolean isCanPlay(Music defaultMisic, Minecraft minecraft) {
            BAPanorama panorama = BAModsHandler.getPanorama();
            return panorama != null && panorama == this.panorama;
        }

        @Override
        public Music play(Music defaultMisic, Minecraft minecraft) {
            return music;
        }

        @Override
        public boolean isMenu(Music defaultMisic, Minecraft minecraft) {
            return true;
        }
    }
}
