package ru.blatfan.blatapi.common.network.playerskin;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import ru.blatfan.blatapi.client.playerskin.PlayerSkinHandler;
import ru.blatfan.blatapi.common.network.BlatApiPacketHandler;
import ru.blatfan.blatapi.common.network.ServerPacket;

import java.util.function.Supplier;

public class PlayerSkinSetPacket extends ServerPacket {
    private final String skin;

    public PlayerSkinSetPacket(String skin) {
        this.skin = skin;
    }

    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        PlayerSkinHandler.setSkin(player, skin);
        for (ServerPlayer serverPlayer : player.getServer().getPlayerList().getPlayers()) {
            BlatApiPacketHandler.sendTo(serverPlayer, new PlayerSkinUpdatePacket(player));
        }
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, PlayerSkinSetPacket.class, PlayerSkinSetPacket::encode, PlayerSkinSetPacket::decode, PlayerSkinSetPacket::handle);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(skin);
    }

    public static PlayerSkinSetPacket decode(FriendlyByteBuf buf) {
        return new PlayerSkinSetPacket(buf.readUtf());
    }
}
