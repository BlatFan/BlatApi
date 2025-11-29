package ru.blatfan.blatapi.common.task;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.BlatApi;

public class EmptyTask extends Task{
    @Override
    public boolean get(Player player) {
        return true;
    }
    
    @Override
    public ResourceLocation getType() {
        return BlatApi.loc("empty");
    }
    
    @Override
    public boolean isVisible() {
        return false;
    }
    
    @Override
    public Component text(Player player) {
        return null;
    }
    public static class Serializer implements ITaskSerializer {
        public static final Serializer INSTANCE = new Serializer();
        protected Serializer(){}
        @Override
        public Task fromJson(JsonObject json) {
            return new EmptyTask();
        }
        
        @Override
        public Task fromNBT(CompoundTag tag) {
            return new EmptyTask();
        }
        
        @Override
        public CompoundTag toNBT(Task task) {
            return new CompoundTag();
        }
    }
}
