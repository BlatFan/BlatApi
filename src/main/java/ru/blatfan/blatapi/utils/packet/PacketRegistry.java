package ru.blatfan.blatapi.utils.packet;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.utils.packet.packet.PacketPlayerFalldamage;

public class PacketRegistry {

  private static final String PROTOCOL_VERSION = Integer.toString(1);
  public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
      .named(new ResourceLocation(BlatApi.MOD_ID, "main_channel"))
      .clientAcceptedVersions(PROTOCOL_VERSION::equals)
      .serverAcceptedVersions(PROTOCOL_VERSION::equals)
      .networkProtocolVersion(() -> PROTOCOL_VERSION)
      .simpleChannel();

  public static void setup() {
    int id = 0;
    INSTANCE.registerMessage(id++, PacketPlayerFalldamage.class, PacketPlayerFalldamage::encode, PacketPlayerFalldamage::decode, PacketPlayerFalldamage::handle);
  }
}
