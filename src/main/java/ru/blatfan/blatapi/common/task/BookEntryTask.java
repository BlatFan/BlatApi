package ru.blatfan.blatapi.common.task;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.common.GuideManager;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public class BookEntryTask extends Task {
    private final boolean visible;
    private final ResourceLocation entry;
    
    @Override
    public boolean get(Player player) {
        if(getEntry()==null) return false;
        if(GuideManager.getEntry(getEntry())==null) return false;
        return GuideManager.getEntry(getEntry()).completed(player);
    }
    
    public static BookEntryTask fromJson(JsonObject json){
        boolean b = !json.has("visible") || json.get("visible").getAsBoolean();
        String stage = json.get("book_entry").getAsString();
        return new BookEntryTask(b, new ResourceLocation(stage));
    }
    
    @Override
    public Component text(Player player) {
        return Component.translatable("task.blatapi.book_entry", getEntry());
    }
}