package ru.blatfan.blatapi.common.task;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;

@AllArgsConstructor
@Getter
public class StageTask extends Task{
    private boolean visible;
    private String stage;
    
    @Override
    public boolean get(Player player) {
        return PlayerStages.has(player, stage);
    }
    
    public static StageTask fromJson(JsonObject json){
        boolean b = !json.has("visible") || json.get("visible").getAsBoolean();
        String stage = json.get("stage").getAsString();
        return new StageTask(b, stage);
    }
    
    @Override
    public Component text(Player player) {
        return Component.translatable("task.blatapi.stage", stage).withStyle(get(player) ? ChatFormatting.GREEN : ChatFormatting.RED);
    }
}