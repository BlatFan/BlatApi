package ru.blatfan.blatapi.common.biome_replacer;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.LevelStem;

import java.util.Set;

public interface BABiomeSource {
    void addPossibleBiomes(Set<Holder<Biome>> biome);
    void setDimension(ResourceKey<LevelStem> dimension);
    ResourceKey<LevelStem> getDimension();
}