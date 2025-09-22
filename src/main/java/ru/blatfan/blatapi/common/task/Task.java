package ru.blatfan.blatapi.common.task;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.utils.Text;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public abstract class Task {
    public static final Map<ResourceLocation, Function<JsonObject, Task>> types = new HashMap<>();
    static {
        types.put(BlatApi.loc("item"), ItemTask::fromJson);
        types.put(BlatApi.loc("position"), PositionTask::fromJson);
        types.put(BlatApi.loc("stage"), StageTask::fromJson);
        types.put(BlatApi.loc("book_entry"), BookEntryTask::fromJson);
        types.put(BlatApi.loc("view_book_entry"), ViewBookEntryTask::fromJson);
        types.put(BlatApi.loc("advancement"), AdvancementTask::fromJson);
        types.put(BlatApi.loc("not"), NotTask::fromJson);
        types.put(BlatApi.loc("craft"), CraftTask::fromJson);
        types.put(BlatApi.loc("kill"), KillTask::fromJson);
        types.put(BlatApi.loc("eat"), EatTask::fromJson);
    }
    
    public abstract boolean get(Player player);
    public abstract boolean isVisible();
    
    public static Task fromJson(JsonObject json){
        ResourceLocation type = ResourceLocation.tryParse(json.get("type").getAsString());
        return types.containsKey(type) ? types.get(type).apply(json) : new EmptyTask();
    }
    
    public abstract Component text(Player player);
    
    public void render(GuiGraphics gui, int x, int y, int mX, int mY, Player player){
        Text text = Text.create(text(player)).withColor(get(player) ? Color.GREEN : Color.RED);
        gui.drawString(Minecraft.getInstance().font, text, x, y+4, text.getColor().getRGB());
    }
}