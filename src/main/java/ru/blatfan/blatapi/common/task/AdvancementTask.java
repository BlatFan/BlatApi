package ru.blatfan.blatapi.common.task;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.utils.collection.Text;

@RequiredArgsConstructor
public class AdvancementTask extends Task {
    private final ResourceLocation advancement;
    private final boolean visible;
    
    public static AdvancementTask fromJson(JsonObject json){
        boolean b = !json.has("visible") || json.get("visible").getAsBoolean();
        ResourceLocation advancement = ResourceLocation.tryParse(json.get("advancement").getAsString());
        return new AdvancementTask(advancement, b);
    }
    
    @Override
    public boolean get(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            Advancement advancement = serverPlayer.getServer().getAdvancements().getAdvancement(this.advancement);
            return advancement != null && serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone();
        }
        return false;
    }
    
    @Override
    public boolean isVisible() {
        return visible;
    }
    
    @Override
    public Component text(Player player) {
        return Text.create("task.blatapi.advancement", advancement);
    }
}
