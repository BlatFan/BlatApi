package ru.blatfan.blatapi.common.task;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.common.task.stage.*;
import ru.blatfan.blatapi.utils.collection.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Task {
    public static final Map<ResourceLocation, ITaskSerializer> types = new HashMap<>();
    
    private static boolean init = false;
    public static void init(){
        if(init) return;
        types.put(BlatApi.loc("has_item"), HasItemTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("item"), ItemTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("position"), PositionTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("book_entry"), BookEntryTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("view_book_entry"), ViewBookEntryTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("advancement"), AdvancementTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("not"), NotTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("craft"), CraftTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("kill"), KillTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("eat"), EatTask.Serializer.INSTANCE);
        
        types.put(BlatApi.loc("stage_bool"), StageBoolTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("stage_double"), StageDoubleTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("stage_float"), StageFloatTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("stage_int"), StageIntTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("stage_item_stack"), StageItemStackTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("stage_long"), StageLongTask.Serializer.INSTANCE);
        types.put(BlatApi.loc("stage_string"), StageStringTask.Serializer.INSTANCE);
        
        init=true;
    }
    
    public abstract boolean get(Player player);
    public abstract ResourceLocation getType();
    public abstract boolean isVisible();
    public abstract Text text(Player player);
    
    public static Task fromJson(JsonObject json){
        ResourceLocation type = ResourceLocation.tryParse(json.get("type").getAsString());
        return types.containsKey(type) ? types.get(type).fromJson(json) : new EmptyTask();
    }
    
    public static List<Task> fromJson(JsonArray json){
        List<Task> list = new ArrayList<>();
        json.forEach(e -> list.add(fromJson(e.getAsJsonObject())));
        return list;
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
    
    public static boolean get(Player player, List<Task> tasks){
        for (Task task : tasks)
            if(!task.get(player)) return false;
        return true;
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