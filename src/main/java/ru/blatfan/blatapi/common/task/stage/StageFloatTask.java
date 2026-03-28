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
public class StageFloatTask extends Task {
    private boolean visible;
    private ResourceLocation stage;
    private float min;
    private float max;
    
    @Override
    public boolean get(Player player) {
        if (!PlayerStages.has(player, stage)) return false;
        float val = PlayerStages.getFloat(player, stage);
        return val > min && val < max;
    }
    
    @Override
    public ResourceLocation getType() {
        return BlatApi.loc("stage_float");
    }
    
    @Override
    public Text text(Player player) {
        return Text.create("task.blatapi.stage_float", stage, min, max).withStyle(get(player) ? ChatFormatting.GREEN : ChatFormatting.RED);
    }
    
    public static class Serializer implements ITaskSerializer {
        public static final Serializer INSTANCE = new Serializer();
        protected Serializer() {}
        
        @Override
        public Task fromJson(JsonObject json) {
            boolean visible = !json.has("visible") || json.get("visible").getAsBoolean();
            ResourceLocation stage = ResourceLocation.parse(json.get("stage").getAsString());
            float min = json.has("min") ? json.get("min").getAsFloat() : Float.NEGATIVE_INFINITY;
            float max = json.has("max") ? json.get("max").getAsFloat() : Float.POSITIVE_INFINITY;
            return new StageFloatTask(visible, stage, min, max);
        }
        
        @Override
        public Task fromNBT(CompoundTag tag) {
            boolean visible = !tag.contains("visible") || tag.getBoolean("visible");
            ResourceLocation stage = ResourceLocation.parse(tag.getString("stage"));
            float min = tag.contains("min") ? tag.getFloat("min") : Float.NEGATIVE_INFINITY;
            float max = tag.contains("max") ? tag.getFloat("max") : Float.POSITIVE_INFINITY;
            return new StageFloatTask(visible, stage, min, max);
        }
        
        @Override
        public CompoundTag toNBT(Task task) {
            CompoundTag tag = new CompoundTag();
            if (task instanceof StageFloatTask t) {
                tag.putBoolean("visible", t.visible);
                tag.putString("stage", t.stage.toString());
                tag.putFloat("min", t.min);
                tag.putFloat("max", t.max);
            }
            return tag;
        }
    }
}
