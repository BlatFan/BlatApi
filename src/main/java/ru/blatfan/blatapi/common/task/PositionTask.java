package ru.blatfan.blatapi.common.task;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.utils.Text;

@AllArgsConstructor
@Getter
public class PositionTask extends Task {
    private final BlockPos pos;
    private final int distance;
    private final boolean visible;
    
    @Override
    public boolean get(Player player) {
        return pos.getCenter().distanceTo(player.getOnPos().getCenter())<=distance;
    }
    
    public static Task fromJson(JsonObject json){
        int x = json.get("x").getAsInt();
        int y = json.get("y").getAsInt();
        int z = json.get("z").getAsInt();
        int d = json.get("distance").getAsInt();
        boolean b = !json.has("visible") || json.get("visible").getAsBoolean();
        return new PositionTask(new BlockPos(x,y,z), d, b);
    }
    
    @Override
    public Component text(Player player) {
        return Text.create("task.blatapi.position", pos.getX(), pos.getY(), pos.getZ()).withStyle(get(player) ? ChatFormatting.GREEN : ChatFormatting.RED);
    }
}