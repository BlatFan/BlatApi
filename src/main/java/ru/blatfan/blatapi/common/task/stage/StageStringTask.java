package ru.blatfan.blatapi.common.task.stage;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;
import ru.blatfan.blatapi.common.task.Task;
import ru.blatfan.blatapi.utils.collection.Text;

@AllArgsConstructor
@Getter
public class StageStringTask extends Task {
    private boolean visible;
    private ResourceLocation stage;
    private String value;
    
    @Override
    public boolean get(Player player) {
        if (!PlayerStages.has(player, stage)) return false;
        return this.value.equals(PlayerStages.getString(player, stage));
    }
    
    @Override
    public ResourceLocation getType() {
        return BlatApi.loc("stage_string");
    }
    
    @Override
    public Text text(Player player) {
        return Text.create("task.blatapi.stage_string", stage, value).withStyle(get(player) ? ChatFormatting.GREEN : ChatFormatting.RED);
    }
    
    public static class Serializer implements ITaskSerializer {
        public static final Serializer INSTANCE = new Serializer();
        protected Serializer() {}
        
        @Override
        public Task fromJson(JsonObject json) {
            boolean visible = !json.has("visible") || json.get("visible").getAsBoolean();
            ResourceLocation stage = ResourceLocation.parse(json.get("stage").getAsString());
            String value = json.get("value").getAsString();
            return new StageStringTask(visible, stage, value);
        }
        
        @Override
        public Task fromNBT(CompoundTag tag) {
            boolean visible = !tag.contains("visible") || tag.getBoolean("visible");
            ResourceLocation stage = ResourceLocation.parse(tag.getString("stage"));
            String value = tag.getString("value");
            return new StageStringTask(visible, stage, value);
        }
        
        @Override
        public CompoundTag toNBT(Task task) {
            CompoundTag tag = new CompoundTag();
            if (task instanceof StageStringTask t) {
                tag.putBoolean("visible", t.visible);
                tag.putString("stage", t.stage.toString());
                tag.putString("value", t.value);
            }
            return tag;
        }
    }
}
