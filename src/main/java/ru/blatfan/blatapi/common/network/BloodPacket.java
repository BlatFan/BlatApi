package ru.blatfan.blatapi.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.particle.ParticleBuilder;
import ru.blatfan.blatapi.client.particle.behavior.SparkParticleBehavior;
import ru.blatfan.blatapi.client.particle.data.ColorParticleData;
import ru.blatfan.blatapi.client.particle.data.GenericParticleData;
import ru.blatfan.blatapi.client.registry.BARenderTypes;
import ru.blatfan.blatapi.common.BARegistry;
import ru.blatfan.blatapi.common.easing.Easing;
import ru.blatfan.blatapi.config.BlatApiClientConfig;

import java.util.function.Supplier;

public class BloodPacket extends PositionClientPacket {
    protected final double size;

    public BloodPacket(double x, double y, double z, double size) {
        super(x, y, z);
        this.size = size;
    }

    public BloodPacket(Vec3 vec, double size) {
        super(vec);
        this.size = size;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void execute(Supplier<NetworkEvent.Context> context) {
        if (BlatApiClientConfig.BLOOD_PARTICLE.get()) {
            Level level = BlatApi.proxy.getLevel();
            double size = this.size;
            if (size > 100) size = 100;
            ParticleBuilder builder = ParticleBuilder.create(BARegistry.Particles.TINY_WISP);
            builder.setBehavior(SparkParticleBehavior.create().build())
                    .setColorData(ColorParticleData.create(1f, 0, 0).build())
                    .setTransparencyData(GenericParticleData.create(0.6f, 0.6f, 0).setEasing(Easing.QUARTIC_OUT).build())
                    .setScaleData(GenericParticleData.create(0.05f, 0.1f, 0).setEasing(Easing.ELASTIC_OUT).build())
                    .setLifetime(20)
                    .randomVelocity(0.5f)
                    .addVelocity(0, 0.1f, 0)
                    .randomOffset(0.05f)
                    .setFriction(0.9f)
                    .enablePhysics()
                    .setGravity(1f)
                    .repeat(level, x, y, z, (int) size / 2, 0.4f);
            builder.setRenderType(BARenderTypes.TRANSLUCENT_PARTICLE)
                    .repeat(level, x, y, z, (int) size, 0.6f);
        }
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, BloodPacket.class, BloodPacket::encode, BloodPacket::decode, BloodPacket::handle);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeDouble(size);
    }

    public static BloodPacket decode(FriendlyByteBuf buf) {
        return new BloodPacket(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
    }
}
