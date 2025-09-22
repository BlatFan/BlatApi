package ru.blatfan.blatapi.common.player_stages;

import lombok.RequiredArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import ru.blatfan.blatapi.fluffy_fur.common.network.CapacitySync;
import ru.blatfan.blatapi.fluffy_fur.common.network.FluffyFurPacketHandler;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class PlayerStagesAdd {
    private final String stage;
    private final boolean b;
    
    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, PlayerStagesAdd.class, PlayerStagesAdd::toBuf, PlayerStagesAdd::buf, PlayerStagesAdd::handler);
    }
    
    private void handler(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        ServerPlayer player = FluffyFurPacketHandler.getPlayer(context);
        context.enqueueWork(() -> PlayerStages.set(player, stage, b));
        context.setPacketHandled(true);
    }
    
    private void toBuf(FriendlyByteBuf buf) {
        buf.writeUtf(stage);
        buf.writeBoolean(b);
    }
    
    public static PlayerStagesAdd buf(FriendlyByteBuf buf) {
        return new PlayerStagesAdd(buf.readUtf(), buf.readBoolean());
    }
}