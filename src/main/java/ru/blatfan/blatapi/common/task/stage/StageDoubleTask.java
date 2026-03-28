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
public class StageDoubleTask extends Task {
    private boolean visible;
    private ResourceLocation stage;
    private double min;
    private double max;
    
    @Override
    public boolean get(Player player) {
        if (!PlayerStages.has(player, stage)) return false;
        double val = PlayerStages.getDouble(player, stage);
        return val > min && val < max;
    }
    
    @Override
    public ResourceLocation getType() {
        return BlatApi.loc("stage_double");
    }
    
    @Override
    public Text text(Player player) {
        return Text.create("task.blatapi.stage_double", stage, min, max).withStyle(get(player) ? ChatFormatting.GREEN : ChatFormatting.RED);
    }
    
    public static class Serializer implements ITaskSerializer {
        public static final Serializer INSTANCE = new Serializer();
        protected Serializer() {}
        
        @Override
        public Task fromJson(JsonObject json) {
            boolean visible = !json.has("visible") || json.get("visible").getAsBoolean();
            ResourceLocation stage = ResourceLocation.parse(json.get("stage").getAsString());
            double min = json.has("min") ? json.get("min").getAsDouble() : Double.NEGATIVE_INFINITY;
            double max = json.has("max") ? json.get("max").getAsDouble() : Double.POSITIVE_INFINITY;
            return new StageDoubleTask(visible, stage, min, max);
        }
        
        @Override
        public Task fromNBT(CompoundTag tag) {
            boolean visible = !tag.contains("visible") || tag.getBoolean("visible");
            ResourceLocation stage = ResourceLocation.parse(tag.getString("stage"));
            double min = tag.contains("min") ? tag.getDouble("min") : Double.NEGATIVE_INFINITY;
            double max = tag.contains("max") ? tag.getDouble("max") : Double.POSITIVE_INFINITY;
            return new StageDoubleTask(visible, stage, min, max);
        }
        
        @Override
        public CompoundTag toNBT(Task task) {
            CompoundTag tag = new CompoundTag();
            if (task instanceof StageDoubleTask t) {
                tag.putBoolean("visible", t.visible);
                tag.putString("stage", t.stage.toString());
                tag.putDouble("min", t.min);
                tag.putDouble("max", t.max);
            }
            return tag;
        }
    }
}
