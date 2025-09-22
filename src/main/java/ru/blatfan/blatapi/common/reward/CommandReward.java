package ru.blatfan.blatapi.common.reward;


import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

@AllArgsConstructor
@Getter
public class CommandReward extends Reward {
    private final String command;
    private final boolean player;
    private final boolean visible;
    
    public String getFormatedCommand(Player player){
        return getCommand()
            .replace(new StringBuffer("%player"), player.getDisplayName().getString())
            .replace(new StringBuffer("%x"), String.valueOf(player.getX()))
            .replace(new StringBuffer("%y"), String.valueOf(player.getY()))
            .replace(new StringBuffer("%z"), String.valueOf(player.getZ()))
            ;
    }
    
    @Override
    public void apply(Player player) {
        CommandSourceStack source=player.createCommandSourceStack();
        if(!isPlayer()) source=source.withPermission(4);
        player.getServer().getCommands().performPrefixedCommand(source, getFormatedCommand(player));
    }
    
    public static CommandReward fromJson(JsonObject json){
        String c = json.get("command").getAsString();
        boolean b = !json.has("visible") || json.get("visible").getAsBoolean();
        boolean p = !json.has("player") || json.get("player").getAsBoolean();
        return new CommandReward(c, p, b);
    }
    
    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString("command", command);
        tag.putBoolean("player", player);
        tag.putBoolean("visible", visible);
        return tag;
    }
    
    public static Reward fromTag(CompoundTag tag) {
        return new CommandReward(
            tag.getString("command"),
            tag.getBoolean("player"),
            tag.getBoolean("visible")
        );
    }
    
    @Override
    public Component text(Player player) {
        return Component.literal(getFormatedCommand(player)).withStyle(ChatFormatting.GRAY);
    }
}