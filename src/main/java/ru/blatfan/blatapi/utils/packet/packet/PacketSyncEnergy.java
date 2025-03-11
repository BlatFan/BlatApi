package ru.blatfan.blatapi.utils.packet.packet;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import ru.blatfan.blatapi.common.core.IHasEnergy;

public class PacketSyncEnergy extends BlatPacket {

  private BlockPos pos;
  private int energy;

  public PacketSyncEnergy(BlockPos p, int fluid) {
    pos = p;
    this.energy = fluid;
  }

  public static void handle(PacketSyncEnergy message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      doWork(message);
    });
    message.done(ctx);
  }

  private static void doWork(PacketSyncEnergy message) {
    BlockEntity te = Minecraft.getInstance().level.getBlockEntity(message.pos);
    if (te instanceof IHasEnergy tile) {
      tile.setEnergy(message.energy);
    }
  }

  public static PacketSyncEnergy decode(FriendlyByteBuf buf) {
    PacketSyncEnergy msg = new PacketSyncEnergy(buf.readBlockPos(),
        buf.readInt());
    return msg;
  }

  public static void encode(PacketSyncEnergy msg, FriendlyByteBuf buf) {
    buf.writeBlockPos(msg.pos);
    buf.writeInt(msg.energy);
  }
}
