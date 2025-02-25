package ru.blatfan.blatapi.fluffy_fur.common.network.playerskin;

import ru.blatfan.blatapi.fluffy_fur.FluffyFur;
import ru.blatfan.blatapi.fluffy_fur.common.capability.IPlayerSkin;
import ru.blatfan.blatapi.fluffy_fur.common.network.ClientPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.UUID;
import java.util.function.Supplier;

public class PlayerSkinUpdatePacket extends ClientPacket {
    private final UUID uuid;
    private CompoundTag tag;

    public PlayerSkinUpdatePacket(UUID uuid, CompoundTag tag) {
        this.uuid = uuid;
        this.tag = tag;
    }

    public PlayerSkinUpdatePacket(Player entity) {
        this.uuid = entity.getUUID();
        entity.getCapability(IPlayerSkin.INSTANCE, null).ifPresent((k) -> {
            this.tag = ((INBTSerializable<CompoundTag>)k).serializeNBT();
        });
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void execute(Supplier<NetworkEvent.Context> context) {
        Level level = FluffyFur.proxy.getLevel();
        Player player = level.getPlayerByUUID(uuid);
        if (player != null) {
            player.getCapability(IPlayerSkin.INSTANCE, null).ifPresent((k) -> {
                ((INBTSerializable<CompoundTag>)k).deserializeNBT(tag);
            });
        }
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, PlayerSkinUpdatePacket.class, PlayerSkinUpdatePacket::encode, PlayerSkinUpdatePacket::decode, PlayerSkinUpdatePacket::handle);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(uuid);
        buf.writeNbt(tag);
    }

    public static PlayerSkinUpdatePacket decode(FriendlyByteBuf buf) {
        return new PlayerSkinUpdatePacket(buf.readUUID(), buf.readNbt());
    }
}
