package ru.blatfan.blatapi.common.network.playerskin;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.particle.ParticleBuilder;
import ru.blatfan.blatapi.client.particle.data.ColorParticleData;
import ru.blatfan.blatapi.client.particle.data.GenericParticleData;
import ru.blatfan.blatapi.client.particle.data.SpinParticleData;
import ru.blatfan.blatapi.common.BARegistry;
import ru.blatfan.blatapi.common.easing.Easing;
import ru.blatfan.blatapi.common.network.PositionClientPacket;

import java.util.function.Supplier;

public class PlayerSkinChangeEffectPacket extends PositionClientPacket {

    public PlayerSkinChangeEffectPacket(double x, double y, double z) {
        super(x, y, z);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void execute(Supplier<NetworkEvent.Context> context) {
        Level level = BlatApi.proxy.getLevel();
        ParticleBuilder.create(BARegistry.Particles.WISP)
                .setColorData(ColorParticleData.create(1, 1, 1, 1, 0, 0).build())
                .setTransparencyData(GenericParticleData.create(0.5f, 0).setEasing(Easing.QUARTIC_OUT).build())
                .setScaleData(GenericParticleData.create(1f, 2, 0).setEasing(Easing.ELASTIC_OUT).build())
                .setSpinData(SpinParticleData.create().randomSpin(0.1f).build())
                .setLifetime(100)
                .randomVelocity(0.35f)
                .flatRandomOffset(1, 1.5f, 1)
                .disableDistanceSpawn()
                .repeat(level, x, y, z, 50);
        level.playSound(BlatApi.proxy.getPlayer(), x, y, z, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.PLAYERS, 1f, 1f);
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, PlayerSkinChangeEffectPacket.class, PlayerSkinChangeEffectPacket::encode, PlayerSkinChangeEffectPacket::decode, PlayerSkinChangeEffectPacket::handle);
    }

    public static PlayerSkinChangeEffectPacket decode(FriendlyByteBuf buf) {
        return decode(PlayerSkinChangeEffectPacket::new, buf);
    }
}
