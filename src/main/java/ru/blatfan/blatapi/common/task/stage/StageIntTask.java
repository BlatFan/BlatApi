package ru.blatfan.blatapi.common.task.stage;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;
import ru.blatfan.blatapi.common.task.Task;
import ru.blatfan.blatapi.utils.collection.Text;

@AllArgsConstructor
@Getter
public class StageIntTask extends Task {
    private boolean visible;
    private ResourceLocation stage;
    private int min;
    private int max;
    
    @Override
    public boolean get(Player player) {
        if (!PlayerStages.has(player, stage)) return false;
        int val = PlayerStages.getInt(player, stage);
        return val > min && val < max;
    }
    
    @Override
    public ResourceLocation getType() {
        return BlatApi.loc("stage_int");
    }
    
    @Override
    public Text text(Player player) {
        return Text.create("task.blatapi.stage_int", stage, min, max).withStyle(get(player) ? ChatFormatting.GREEN : ChatFormatting.RED);
    }
    
    public static class Serializer implements ITaskSerializer {
        public static final Serializer INSTANCE = new Serializer();
        protected Serializer() {}
        
        @Override
        public Task fromJson(JsonObject json) {
            boolean visible = !json.has("visible") || json.get("visible").getAsBoolean();
            ResourceLocation stage = ResourceLocation.parse(json.get("stage").getAsString());
            int min = json.has("min") ? json.get("min").getAsInt() : Integer.MIN_VALUE;
            int max = json.has("max") ? json.get("max").getAsInt() : Integer.MAX_VALUE;
            return new StageIntTask(visible, stage, min, max);
        }
        
        @Override
        public Task fromNBT(CompoundTag tag) {
            boolean visible = !tag.contains("visible") || tag.getBoolean("visible");
            ResourceLocation stage = ResourceLocation.parse(tag.getString("stage"));
            int min = tag.contains("min") ? tag.getInt("min") : Integer.MIN_VALUE;
            int max = tag.contains("max") ? tag.getInt("max") : Integer.MAX_VALUE;
            return new StageIntTask(visible, stage, min, max);
        }
        
        @Override
        public CompoundTag toNBT(Task task) {
            CompoundTag tag = new CompoundTag();
            if (task instanceof StageIntTask t) {
                tag.putBoolean("visible", t.visible);
                tag.putString("stage", t.stage.toString());
                tag.putInt("min", t.min);
                tag.putInt("max", t.max);
            }
            return tag;
        }
    }
}
