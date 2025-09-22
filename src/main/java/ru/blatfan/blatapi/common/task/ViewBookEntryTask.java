package ru.blatfan.blatapi.common.task;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.common.GuideManager;

@AllArgsConstructor
@Getter
public class ViewBookEntryTask extends Task {
    private final boolean visible;
    private final ResourceLocation entry;
    
    @Override
    public boolean get(Player player) {
        if(getEntry()==null) return false;
        if(GuideManager.getEntry(getEntry())==null) return false;
        return GuideManager.getEntry(getEntry()).visible(player);
    }
    
    public static ViewBookEntryTask fromJson(JsonObject json){
        boolean b = !json.has("visible") || json.get("visible").getAsBoolean();
        String stage = json.get("book_entry").getAsString();
        return new ViewBookEntryTask(b, new ResourceLocation(stage));
    }
    
    @Override
    public Component text(Player player) {
        return Component.translatable("task.blatapi.book_entry", getEntry());
    }
}