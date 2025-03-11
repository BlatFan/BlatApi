
package ru.blatfan.blatapi.common.biome_replacer;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import terrablender.worldgen.IExtendedParameterList;

public class TerrablenderHelper {

    public static Holder<Biome> getCurrentBiome(MultiNoiseBiomeSource source, int x, int y, int z, Climate.Sampler sampler) {
        if (source.parameters() instanceof IExtendedParameterList<?> terrablenderParameters) {
            if (terrablenderParameters.isInitialized()) {
                return (Holder) terrablenderParameters.findValuePositional(sampler.sample(x, y, z), x, y, z);
            }
        }

        return source.parameters().findValue(sampler.sample(x, y, z));
    }
}
