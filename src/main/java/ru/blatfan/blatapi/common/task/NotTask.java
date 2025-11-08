package ru.blatfan.blatapi.common.task;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.utils.collection.Text;

@AllArgsConstructor
@Getter
public class NotTask extends Task {
    private final boolean visible;
    private final Task task;
    
    @Override
    public boolean get(Player player) {
        return !task.get(player);
    }
    
    public static NotTask fromJson(JsonObject json){
        boolean b = !json.has("visible") || json.get("visible").getAsBoolean();
        Task task = Task.fromJson(json.get("task").getAsJsonObject());
        return new NotTask(b, task);
    }
    
    @Override
    public Component text(Player player) {
        return Text.create("!").add(task.text(player)).asComponent();
    }
}