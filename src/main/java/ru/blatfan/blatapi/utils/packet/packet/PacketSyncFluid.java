package ru.blatfan.blatapi.utils.packet.packet;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;
import ru.blatfan.blatapi.core.IHasFluid;

public class PacketSyncFluid extends BlatPacket {

  private BlockPos pos;
  private FluidStack fluid;

  public PacketSyncFluid(BlockPos p, FluidStack fluid) {
    pos = p;
    this.fluid = fluid;
  }

  public static void handle(PacketSyncFluid message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      doWork(message);
    });
    message.done(ctx);
  }

  private static void doWork(PacketSyncFluid message) {
    BlockEntity te = Minecraft.getInstance().level.getBlockEntity(message.pos);
    if (te instanceof IHasFluid tile) {
      tile.setFluid(message.fluid);
    }
  }

  public static PacketSyncFluid decode(FriendlyByteBuf buf) {
    PacketSyncFluid msg = new PacketSyncFluid(buf.readBlockPos(),
        FluidStack.loadFluidStackFromNBT(buf.readNbt()));
    return msg;
  }

  public static void encode(PacketSyncFluid msg, FriendlyByteBuf buf) {
    buf.writeBlockPos(msg.pos);
    CompoundTag tags = new CompoundTag();
    if (msg.fluid != null) {
      msg.fluid.writeToNBT(tags);
    }
    buf.writeNbt(tags);
  }
}
