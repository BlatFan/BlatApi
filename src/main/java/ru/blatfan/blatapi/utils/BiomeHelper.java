package ru.blatfan.blatapi.utils;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.LevelStem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BiomeHelper {
    public static Set<Holder<Biome>> overworldPossibleBiomes = new HashSet<>();
    public static Set<Holder<Biome>> netherPossibleBiomes = new HashSet<>();
    public static final List<BiomeReplacer> overworldBiomes = new ArrayList<>();
    public static final List<BiomeReplacer> netherBiomes = new ArrayList<>();

    public static List<BiomeReplacer> biomesForDimension(ResourceKey<LevelStem> dimension) {
        if (dimension.equals(LevelStem.OVERWORLD)) {
            return overworldBiomes;
        }
        else return netherBiomes;
    }

    public record BiomeReplacer(ResourceKey<LevelStem> dimension, ResourceKey<Biome> biome, ResourceKey<Biome> canReplace, double rarity, int size, ResourceLocation id) {}
}
