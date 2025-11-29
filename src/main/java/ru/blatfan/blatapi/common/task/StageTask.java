package ru.blatfan.blatapi.common.task;

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

@AllArgsConstructor
@Getter
public class StageTask extends Task{
    private boolean visible;
    private String stage;
    
    @Override
    public boolean get(Player player) {
        return PlayerStages.has(player, stage);
    }
    
    @Override
    public ResourceLocation getType() {
        return BlatApi.loc("stage");
    }
    
    @Override
    public Component text(Player player) {
        return Component.translatable("task.blatapi.stage", stage).withStyle(get(player) ? ChatFormatting.GREEN : ChatFormatting.RED);
    }
    
    public static class Serializer implements ITaskSerializer {
        public static final Serializer INSTANCE = new Serializer();
        protected Serializer(){}
        @Override
        public Task fromJson(JsonObject json) {
            boolean b = !json.has("visible") || json.get("visible").getAsBoolean();
            String stage = json.get("stage").getAsString();
            return new StageTask(b, stage);
        }
        
        @Override
        public Task fromNBT(CompoundTag tag) {
            String stage = tag.getString("stage");
            boolean b = !tag.contains("visible") || tag.getBoolean("visible");
            return new StageTask(b, stage);
        }
        
        @Override
        public CompoundTag toNBT(Task task) {
            CompoundTag tag = new CompoundTag();
            if(task instanceof StageTask task1){
                tag.putBoolean("visible", task1.visible);
                tag.putString("stage", task1.stage);
            }
            return tag;
        }
    }
}