package ru.blatfan.blatapi.common.task;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import net.minecraft.advancements.Advancement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.utils.collection.Text;

@RequiredArgsConstructor
public class AdvancementTask extends Task {
    private final ResourceLocation advancement;
    private final boolean visible;
    
    @Override
    public boolean get(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            Advancement advancement = serverPlayer.getServer().getAdvancements().getAdvancement(this.advancement);
            return advancement != null && serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone();
        }
        return false;
    }
    
    @Override
    public ResourceLocation getType() {
        return BlatApi.loc("advancement");
    }
    
    @Override
    public boolean isVisible() {
        return visible;
    }
    
    @Override
    public Component text(Player player) {
        return Text.create("task.blatapi.advancement", advancement);
    }
    
    public static class Serializer implements ITaskSerializer {
        public static final Serializer INSTANCE = new Serializer();
        protected Serializer(){}
        @Override
        public Task fromJson(JsonObject json) {
            boolean b = !json.has("visible") || json.get("visible").getAsBoolean();
            ResourceLocation advancement = ResourceLocation.tryParse(json.get("advancement").getAsString());
            return new AdvancementTask(advancement, b);
        }
        
        @Override
        public Task fromNBT(CompoundTag tag) {
            String stage = tag.getString("advancement");
            boolean b = !tag.contains("visible") || tag.getBoolean("visible");
            return new AdvancementTask(ResourceLocation.tryParse(stage), b);
        }
        
        @Override
        public CompoundTag toNBT(Task task) {
            CompoundTag tag = new CompoundTag();
            if(task instanceof AdvancementTask task1){
                tag.putBoolean("visible", task1.visible);
                tag.putString("advancement", task1.advancement.toString());
            }
            return tag;
        }
    }
}
