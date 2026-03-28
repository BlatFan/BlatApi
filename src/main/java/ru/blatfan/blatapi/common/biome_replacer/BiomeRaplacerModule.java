package ru.blatfan.blatapi.common.biome_replacer;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.utils.BiomeHelper;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = BlatApi.MOD_ID)
public class BiomeRaplacerModule {
    public static final Logger LOGGER = LoggerFactory.getLogger("BiomeReplacer");
    public static RegistryAccess registryAccess;
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        registryAccess = event.getServer().registryAccess();
        
        Registry<LevelStem> levelStems = registryAccess.registryOrThrow(Registries.LEVEL_STEM);
        for (ResourceKey<LevelStem> dimension : levelStems.registryKeySet()) {
            Optional<Holder.Reference<LevelStem>> optionalLevelStem = levelStems.getHolder(dimension);
            if (optionalLevelStem.isPresent() && optionalLevelStem.get().value().generator().getBiomeSource() instanceof BABiomeSource biomeSource) {
                if (dimension.equals(LevelStem.OVERWORLD)) {
                    // initializes BiomeReplacer for the Overworld
                    biomeSource.setDimension(LevelStem.OVERWORLD);
                    biomeSource.addPossibleBiomes(BiomeHelper.overworldPossibleBiomes);
                }
                else if (dimension.equals(LevelStem.NETHER)) {
                    // initializes BiomeReplacer for the Nether
                    biomeSource.setDimension(LevelStem.NETHER);
                    biomeSource.addPossibleBiomes(BiomeHelper.netherPossibleBiomes);
                }
                //TODO: End Biomes
            }
        }
    }
}
