package ru.blatfan.blatapi.fluffy_fur.client.particle.type;

import ru.blatfan.blatapi.fluffy_fur.client.particle.ItemParticle;
import ru.blatfan.blatapi.fluffy_fur.client.particle.options.ItemParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;

public class ItemParticleType extends AbstractParticleType<ItemParticleOptions> {

    public ItemParticleType() {
        super();
    }

    public static class Factory implements ParticleProvider<ItemParticleOptions> {
        public Factory(SpriteSet sprite) {}

        @Override
        public Particle createParticle(ItemParticleOptions options, ClientLevel level, double x, double y, double z, double mx, double my, double mz) {
            return new ItemParticle(level, options, x, y, z, mx, my, mz);
        }
    }
}