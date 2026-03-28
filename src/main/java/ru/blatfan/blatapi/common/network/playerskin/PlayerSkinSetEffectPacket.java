package ru.blatfan.blatapi.common.network.playerskin;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import ru.blatfan.blatapi.client.playerskin.PlayerSkinHandler;
import ru.blatfan.blatapi.common.network.BlatApiPacketHandler;
import ru.blatfan.blatapi.common.network.ServerPacket;

import java.util.function.Supplier;

public class PlayerSkinSetEffectPacket extends ServerPacket {
    private final String effect;

    public PlayerSkinSetEffectPacket(String effectId) {
        this.effect = effectId;
    }

    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        PlayerSkinHandler.setSkinEffect(player, effect);
        for (ServerPlayer serverPlayer : player.getServer().getPlayerList().getPlayers()) {
            BlatApiPacketHandler.sendTo(serverPlayer, new PlayerSkinUpdatePacket(player));
        }
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, PlayerSkinSetEffectPacket.class, PlayerSkinSetEffectPacket::encode, PlayerSkinSetEffectPacket::decode, PlayerSkinSetEffectPacket::handle);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(effect);
    }

    public static PlayerSkinSetEffectPacket decode(FriendlyByteBuf buf) {
        return new PlayerSkinSetEffectPacket(buf.readUtf());
    }
}
