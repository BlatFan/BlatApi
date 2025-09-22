package ru.blatfan.blatapi.fluffy_fur.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import ru.blatfan.blatapi.common.reward.ClientReward;
import ru.blatfan.blatapi.common.reward.Reward;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RewardPacket {
    private final List<Reward> rewards;
    
    public RewardPacket(List<Reward> rewards) {
        this.rewards = rewards;
    }
    public RewardPacket(FriendlyByteBuf buf) {
        this.rewards=new ArrayList<>();
        for(int i=0; i<buf.readInt(); i++)
            rewards.add(Reward.fromTag(buf.readNbt()));
    }
    
    public static void buffer(RewardPacket message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.rewards.size());
        for(Reward r : message.rewards)
            buffer.writeNbt(r.toTag());
    }
    public static void register(SimpleChannel channel, int id){
        channel.registerMessage(id, RewardPacket.class, RewardPacket::buffer, RewardPacket::new, RewardPacket::handler);
    }
    
    private static void handler(RewardPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        ServerPlayer player = FluffyFurPacketHandler.getPlayer(context);
        context.enqueueWork(() -> {
            for (Reward reward : packet.rewards)
                if(!(reward instanceof ClientReward)) reward.apply(player);
        });
        context.setPacketHandled(true);
    }
}
