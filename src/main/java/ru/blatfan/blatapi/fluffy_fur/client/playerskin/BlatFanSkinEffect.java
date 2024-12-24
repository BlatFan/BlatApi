package ru.blatfan.blatapi.fluffy_fur.client.playerskin;

import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.fluffy_fur.client.particle.ParticleBuilder;
import ru.blatfan.blatapi.fluffy_fur.client.particle.data.GenericParticleData;
import ru.blatfan.blatapi.fluffy_fur.client.particle.data.SpinParticleData;
import ru.blatfan.blatapi.fluffy_fur.common.easing.Easing;
import ru.blatfan.blatapi.fluffy_fur.registry.client.FluffyFurParticles;

public class BlatFanSkinEffect extends PlayerSkinEffect{
    public BlatFanSkinEffect(String id) {
        super(id);
    }
    
    @Override
    public void tick(Player player) {
        if (random.nextFloat() < 0.2f) {
            ParticleBuilder.create(FluffyFurParticles.EMERALD)
                .setTransparencyData(GenericParticleData.create(0.2f, 0.5f, 0).setEasing(Easing.QUINTIC_IN_OUT).build())
                .setScaleData(GenericParticleData.create(0.02f, 0.05f, 0).setEasing(Easing.QUINTIC_IN_OUT).build())
                .setSpinData(SpinParticleData.create().randomSpin(0.01f).build())
                .setLifetime(45)
                .randomVelocity(0.035f)
                .flatRandomOffset(player.getBbWidth() / 2f, player.getBbHeight() / 2f, player.getBbWidth() / 2f)
                .spawn(player.level(), player.getX(), player.getY() + (player.getBbHeight() / 2f), player.getZ());
        }
    }
}
