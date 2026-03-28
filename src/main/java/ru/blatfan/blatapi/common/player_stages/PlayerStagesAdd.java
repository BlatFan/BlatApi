package ru.blatfan.blatapi.common.player_stages;

import lombok.RequiredArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import ru.blatfan.blatapi.common.network.BlatApiPacketHandler;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class PlayerStagesAdd<T> {
    private final ResourceLocation stage;
    private final PlayerStages.Value<T> b;
    
    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, PlayerStagesAdd.class, PlayerStagesAdd::toBuf, PlayerStagesAdd::buf, PlayerStagesAdd::handler);
    }
    
    private void handler(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        ServerPlayer player = BlatApiPacketHandler.getPlayer(context);
        context.enqueueWork(() -> PlayerStages.set(player, stage, b.getValue()));
        context.setPacketHandled(true);
    }
    
    private void toBuf(FriendlyByteBuf buf) {
        buf.writeResourceLocation(stage);
        buf.writeUtf(b.getType().toString());
        buf.writeNbt(b.serializeNBT());
    }
    
    public static PlayerStagesAdd<?> buf(FriendlyByteBuf buf) {
        return new PlayerStagesAdd<>(buf.readResourceLocation(), PlayerStages.Value.deserialize(ResourceLocation.tryParse(buf.readUtf()), buf.readNbt()));
    }
}