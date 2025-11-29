package ru.blatfan.blatapi.common.task;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.common.GuideManager;

@AllArgsConstructor
@Getter
public class BookEntryTask extends Task {
    protected final boolean visible;
    protected final ResourceLocation entry;
    
    @Override
    public boolean get(Player player) {
        if(getEntry()==null) return false;
        if(GuideManager.getEntry(getEntry())==null) return false;
        return GuideManager.getEntry(getEntry()).completed(player);
    }
    
    @Override
    public ResourceLocation getType() {
        return BlatApi.loc("book_entry");
    }
    
    @Override
    public Component text(Player player) {
        return Component.translatable("task.blatapi.book_entry", getEntry());
    }
    
    public static class Serializer implements ITaskSerializer {
        public static final Serializer INSTANCE = new Serializer();
        protected Serializer(){}
        @Override
        public Task fromJson(JsonObject json) {
            boolean b = !json.has("visible") || json.get("visible").getAsBoolean();
            String stage = json.get("book_entry").getAsString();
            return new BookEntryTask(b, new ResourceLocation(stage));
        }
        
        @Override
        public Task fromNBT(CompoundTag tag) {
            String stage = tag.getString("book_entry");
            boolean b = !tag.contains("visible") || tag.getBoolean("visible");
            return new BookEntryTask(b, ResourceLocation.tryParse(stage));
        }
        
        @Override
        public CompoundTag toNBT(Task task) {
            CompoundTag tag = new CompoundTag();
            if(task instanceof BookEntryTask task1){
                tag.putBoolean("visible", task1.visible);
                tag.putString("stage", task1.entry.toString());
            }
            return tag;
        }
    }
}