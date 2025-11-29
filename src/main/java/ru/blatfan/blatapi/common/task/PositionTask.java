package ru.blatfan.blatapi.common.task;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.utils.collection.Text;

@AllArgsConstructor
@Getter
public class PositionTask extends Task {
    private final BlockPos pos;
    private final int distance;
    private final boolean visible;
    
    @Override
    public boolean get(Player player) {
        return pos.getCenter().distanceTo(player.getOnPos().getCenter())<=distance;
    }
    
    @Override
    public ResourceLocation getType() {
        return BlatApi.loc("position");
    }
    
    @Override
    public Component text(Player player) {
        return Text.create("task.blatapi.position", pos.getX(), pos.getY(), pos.getZ()).withStyle(get(player) ? ChatFormatting.GREEN : ChatFormatting.RED);
    }
    
    public static class Serializer implements ITaskSerializer {
        public static final Serializer INSTANCE = new Serializer();
        protected Serializer(){}
        @Override
        public Task fromJson(JsonObject json) {
            int x = json.get("x").getAsInt();
            int y = json.get("y").getAsInt();
            int z = json.get("z").getAsInt();
            int d = json.get("distance").getAsInt();
            boolean b = !json.has("visible") || json.get("visible").getAsBoolean();
            return new PositionTask(new BlockPos(x,y,z), d, b);
        }
        
        @Override
        public Task fromNBT(CompoundTag tag) {
            int x = tag.getInt("x");
            int y = tag.getInt("y");
            int z = tag.getInt("z");
            int d = tag.getInt("distance");
            boolean b = !tag.contains("visible") || tag.getBoolean("visible");
            return new PositionTask(new BlockPos(x,y,z), d, b);
        }
        
        @Override
        public CompoundTag toNBT(Task task) {
            CompoundTag tag = new CompoundTag();
            if(task instanceof PositionTask task1){
                tag.putBoolean("visible", task1.visible);
                tag.putInt("x", task1.getPos().getX());
                tag.putInt("y", task1.getPos().getY());
                tag.putInt("z", task1.getPos().getZ());
                tag.putInt("distance", task1.distance);
            }
            return tag;
        }
    }
}