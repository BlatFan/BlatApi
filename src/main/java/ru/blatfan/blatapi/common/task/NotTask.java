package ru.blatfan.blatapi.common.task;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.utils.collection.Text;

@AllArgsConstructor
@Getter
public class NotTask extends Task {
    private final boolean visible;
    private final Task task;
    
    @Override
    public boolean get(Player player) {
        return !task.get(player);
    }
    
    @Override
    public ResourceLocation getType() {
        return BlatApi.loc("not");
    }
    
    @Override
    public Component text(Player player) {
        return Text.create("!").add(task.text(player)).asComponent();
    }
    
    public static class Serializer implements ITaskSerializer {
        public static final Serializer INSTANCE = new Serializer();
        protected Serializer(){}
        @Override
        public Task fromJson(JsonObject json) {
            boolean b = !json.has("visible") || json.get("visible").getAsBoolean();
            Task task = Task.fromJson(json.get("task").getAsJsonObject());
            return new NotTask(b, task);
        }
        
        @Override
        public Task fromNBT(CompoundTag tag) {
            Task task = Task.fromNBT(tag.getCompound("task"));
            boolean b = !tag.contains("visible") || tag.getBoolean("visible");
            return new NotTask(b, task);
        }
        
        @Override
        public CompoundTag toNBT(Task task) {
            CompoundTag tag = new CompoundTag();
            if(task instanceof NotTask task1){
                tag.putBoolean("visible", task1.visible);
                tag.put("task", Task.toNBT(task1.task));
            }
            return tag;
        }
    }
}