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
public class StageLongTask extends Task {
    private boolean visible;
    private ResourceLocation stage;
    private long min;
    private long max;
    
    @Override
    public boolean get(Player player) {
        if (!PlayerStages.has(player, stage)) return false;
        long val = PlayerStages.getLong(player, stage);
        return val > min && val < max;
    }
    
    @Override
    public ResourceLocation getType() {
        return BlatApi.loc("stage_long");
    }
    
    @Override
    public Text text(Player player) {
        return Text.create("task.blatapi.stage_long", stage, min, max).withStyle(get(player) ? ChatFormatting.GREEN : ChatFormatting.RED);
    }
    
    public static class Serializer implements ITaskSerializer {
        public static final Serializer INSTANCE = new Serializer();
        protected Serializer() {}
        
        @Override
        public Task fromJson(JsonObject json) {
            boolean visible = !json.has("visible") || json.get("visible").getAsBoolean();
            ResourceLocation stage = ResourceLocation.parse(json.get("stage").getAsString());
            long min = json.has("min") ? json.get("min").getAsLong() : Long.MIN_VALUE;
            long max = json.has("max") ? json.get("max").getAsLong() : Long.MAX_VALUE;
            return new StageLongTask(visible, stage, min, max);
        }
        
        @Override
        public Task fromNBT(CompoundTag tag) {
            boolean visible = !tag.contains("visible") || tag.getBoolean("visible");
            ResourceLocation stage = ResourceLocation.parse(tag.getString("stage"));
            long min = tag.contains("min") ? tag.getLong("min") : Long.MIN_VALUE;
            long max = tag.contains("max") ? tag.getLong("max") : Long.MAX_VALUE;
            return new StageLongTask(visible, stage, min, max);
        }
        
        @Override
        public CompoundTag toNBT(Task task) {
            CompoundTag tag = new CompoundTag();
            if (task instanceof StageLongTask t) {
                tag.putBoolean("visible", t.visible);
                tag.putString("stage", t.stage.toString());
                tag.putLong("min", t.min);
                tag.putLong("max", t.max);
            }
            return tag;
        }
    }
}
