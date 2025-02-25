package ru.blatfan.blatapi.fluffy_fur.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Random;
import java.util.function.Supplier;

public abstract class ServerPacket {
    public static final Random random = new Random();

    public void encode(FriendlyByteBuf buf) {}

    public final void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> execute(context));
        context.get().setPacketHandled(true);
    }

    public void execute(Supplier<NetworkEvent.Context> context) {}
}
