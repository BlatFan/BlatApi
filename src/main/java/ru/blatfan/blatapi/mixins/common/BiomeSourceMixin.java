package ru.blatfan.blatapi.mixins.common;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.dimension.LevelStem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import ru.blatfan.blatapi.common.biome_replacer.BABiomeSource;
import ru.blatfan.blatapi.common.biome_replacer.BiomeRaplacerModule;

import java.util.Set;
import java.util.function.Supplier;

@Mixin(BiomeSource.class)
public class BiomeSourceMixin implements BABiomeSource {
    @Shadow
    @Mutable
    public Supplier<Set<Holder<Biome>>> possibleBiomes;
    @Unique
    private boolean blatapi$hasMergedPossibleBiomes = false;

    @Unique
    private ResourceKey<LevelStem> blatapi$currentDimension = null;

    @Override
    public void addPossibleBiomes(Set<Holder<Biome>> biomes) {
        if(blatapi$hasMergedPossibleBiomes) {
            return;
        }

        ImmutableSet.Builder<Holder<Biome>> builder = ImmutableSet.builder();
        builder.addAll(this.possibleBiomes.get());
        builder.addAll(biomes);
        this.possibleBiomes = Suppliers.memoize(builder::build);
        this.blatapi$hasMergedPossibleBiomes = true;
        BiomeRaplacerModule.LOGGER.info("BFBiomeSource successfully initialized for " + blatapi$currentDimension.location());
    }

    @Override
    public void setDimension(ResourceKey<LevelStem> dimension) {
        this.blatapi$currentDimension = dimension;
    }

    @Override
    public ResourceKey<LevelStem> getDimension() {
        return this.blatapi$currentDimension;
    }
}
