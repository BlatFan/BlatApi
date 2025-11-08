package ru.blatfan.blatapi.utils;

import com.mojang.brigadier.context.CommandContext;
import lombok.experimental.UtilityClass;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.utils.collection.Text;

@UtilityClass
public class ChatUtil {

  public static Text ilang(String message) {
    return Text.create(message);
  }

  public static void addChatMessage(Player player, Component message) {
    if (player.level().isClientSide) {
      player.sendSystemMessage(message);
    }
  }

  public static void addChatMessage(Player player, String message) {
    addChatMessage(player, ilang(message));
  }

  public static void addServerChatMessage(Player player, String message) {
    addServerChatMessage(player, ilang(message));
  }

  public static void addServerChatMessage(Player player, Component message) {
    if (!player.level().isClientSide) {
      player.sendSystemMessage(message);
    }
  }

  public static String blockPosToString(BlockPos pos) {
    return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
  }

  public static void sendStatusMessage(Player player, String message) {
    player.displayClientMessage(ilang(message), true);
  }

  public static void sendStatusMessage(Player player, Component nameTextComponent) {
    if (player.level().isClientSide) {
      player.displayClientMessage(nameTextComponent, true);
    }
  }

  public static String lang(String message) {
    return ilang(message).getString();
  }

  public static void sendFeedback(CommandContext<CommandSourceStack> ctx, String string) {
    ctx.getSource().sendSuccess(() -> ilang(string), false);
  }
}
