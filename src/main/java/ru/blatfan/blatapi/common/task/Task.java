package ru.blatfan.blatapi.common.task;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.utils.collection.Text;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class Task {
    public static final Map<ResourceLocation, ITaskSerializer> types = new HashMap<>();
    public static void init(){
        types.put(BlatApi.loc("has_item"), HasItemTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("item"), ItemTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("position"), PositionTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("stage"), StageTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("book_entry"), BookEntryTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("view_book_entry"), ViewBookEntryTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("advancement"), AdvancementTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("not"), NotTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("craft"), CraftTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("kill"), KillTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("eat"), EatTask.Serializer.INSTANCE);
    }
    
    public abstract boolean get(Player player);
    public abstract ResourceLocation getType();
    public abstract boolean isVisible();
    public abstract Component text(Player player);
    
    public static Task fromJson(JsonObject json){
        ResourceLocation type = ResourceLocation.tryParse(json.get("type").getAsString());
        return types.containsKey(type) ? types.get(type).fromJson(json) : new EmptyTask();
    }
    
    public static Task fromNBT(CompoundTag tag){
        ResourceLocation type = ResourceLocation.tryParse(tag.getString("type"));
        return types.containsKey(type) ? types.get(type).fromNBT(tag) : new EmptyTask();
    }
    
    public static CompoundTag toNBT(Task task){
        ResourceLocation type = task.getType();
        CompoundTag tag = types.containsKey(type) ? types.get(type).toNBT(task) : new CompoundTag();
        tag.putString("type", type.toString());
        return tag;
    }
    
    public ITaskSerializer getSerializer(){
        return types.getOrDefault(getType(), EmptyTask.Serializer.INSTANCE);
    }
    
    public void render(GuiGraphics gui, int x, int y, int mX, int mY, Player player){
        Text text = Text.create(text(player)).withColor(get(player) ? Color.GREEN : Color.RED);
        gui.drawString(Minecraft.getInstance().font, text, x, y+4, text.getColor().getRGB());
    }
    
    public interface ITaskSerializer {
        Task fromJson(JsonObject json);
        Task fromNBT(CompoundTag tag);
        CompoundTag toNBT(Task task);
    }
}