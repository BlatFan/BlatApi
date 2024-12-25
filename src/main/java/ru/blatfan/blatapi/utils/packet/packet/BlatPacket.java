package ru.blatfan.blatapi.utils.packet.packet;

import java.util.function.Supplier;
import net.minecraftforge.network.NetworkEvent.Context;

public class BlatPacket {
  public void done(Supplier<Context> ctx) {
    ctx.get().setPacketHandled(true);
  }
}
