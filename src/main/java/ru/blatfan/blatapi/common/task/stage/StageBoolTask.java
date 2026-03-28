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
public class StageBoolTask extends Task {
    private boolean visible;
    private ResourceLocation stage;
    
    @Override
    public boolean get(Player player) {
        return PlayerStages.has(player, stage) && PlayerStages.getBool(player, stage);
    }
    
    @Override
    public ResourceLocation getType() {
        return BlatApi.loc("stage_bool");
    }
    
    @Override
    public Text text(Player player) {
        return Text.create("task.blatapi.stage_bool", stage).withStyle(get(player) ? ChatFormatting.GREEN : ChatFormatting.RED);
    }
    
    public static class Serializer implements ITaskSerializer {
        public static final Serializer INSTANCE = new Serializer();
        protected Serializer(){}
        @Override
        public Task fromJson(JsonObject json) {
            boolean b = !json.has("visible") || json.get("visible").getAsBoolean();
            ResourceLocation stage = ResourceLocation.parse(json.get("stage").getAsString());
            return new StageBoolTask(b, stage);
        }
        
        @Override
        public Task fromNBT(CompoundTag tag) {
            ResourceLocation stage = ResourceLocation.parse(tag.getString("stage"));
            boolean b = !tag.contains("visible") || tag.getBoolean("visible");
            return new StageBoolTask(b, stage);
        }
        
        @Override
        public CompoundTag toNBT(Task task) {
            CompoundTag tag = new CompoundTag();
            if(task instanceof StageBoolTask task1){
                tag.putBoolean("visible", task1.visible);
                tag.putString("stage", task1.stage.toString());
            }
            return tag;
        }
    }
}