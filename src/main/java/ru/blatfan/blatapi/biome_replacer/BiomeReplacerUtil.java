package ru.blatfan.blatapi.biome_replacer;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.LevelStem;
import ru.blatfan.blatapi.utils.BiomeHelper;

import static ru.blatfan.blatapi.utils.BiomeHelper.*;


public class BiomeReplacerUtil {
    /**
     * Registers a BiomeReplacer for The Overworld
     * @param replaceBiome      - biomes it can replace
     * @param withBiome         - the biome to swap out with
     * @param rarity            - rarity of this BiomeReplacer
     * @param size              - replacement radius of this BiomeReplacer (values lower than 12 may produce micro-biomes)
     * @param id                - unique named identifier for your BiomeReplacer
     * @param registryAccess    - pass in a RegistryAccess, preferably from ServerAboutToStartEvent
     */
    public static void replaceOverworldBiome(ResourceKey<Biome> replaceBiome, ResourceKey<Biome> withBiome, double rarity, int size, ResourceLocation id, RegistryAccess registryAccess) {
        overworldPossibleBiomes.add(registryAccess.registryOrThrow(Registries.BIOME).getHolderOrThrow(withBiome));
        overworldBiomes.add(new BiomeReplacer(LevelStem.OVERWORLD, withBiome, replaceBiome, rarity, size, id));
    }

    /**
     * Registers a BiomeReplacer for The Nether
     * @param replaceBiome      - biomes it can replace
     * @param withBiome         - the biome to swap out with
     * @param rarity            - rarity of this BiomeReplacer
     * @param size              - replacement radius of this BiomeReplacer (values lower than 12 may produce micro-biomes)
     * @param id                - unique named identifier for your BiomeReplacer
     * @param registryAccess    - pass in a RegistryAccess, preferably from ServerAboutToStartEvent
     */
    public static void replaceNetherBiome(ResourceKey<Biome> replaceBiome, ResourceKey<Biome> withBiome, double rarity, int size, ResourceLocation id, RegistryAccess registryAccess) {
        netherPossibleBiomes.add(registryAccess.registryOrThrow(Registries.BIOME).getHolderOrThrow(withBiome));
        netherBiomes.add(new BiomeReplacer(LevelStem.NETHER, withBiome, replaceBiome, rarity, size, id));
    }
}
