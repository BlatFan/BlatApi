package ru.blatfan.blatapi.common.guide_book;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import ru.blatfan.blatapi.common.task.Task;
import ru.blatfan.blatapi.utils.RecipeUtil;
import ru.blatfan.blatapi.utils.Text;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record GuideBookCategory(int sort, ResourceLocation book, List<Background> backgrounds, Component title, Color titleColor, ItemStack icon, List<Task> tasks) {
    public static GuideBookCategory json(JsonElement element) {
        JsonObject json = element.getAsJsonObject();
        int sort = json.get("sort").getAsInt();
        Component title = Text.create(json.get("title").getAsString());
        Color titleColor = Color.decode(json.get("title_color").getAsString());
        ItemStack icon = RecipeUtil.itemStackFromJson(json.get("icon").getAsJsonObject());
        List<Task> tasks = new ArrayList<>();
        if(json.has("tasks"))
            for (JsonElement el : json.get("tasks").getAsJsonArray())
                tasks.add(Task.fromJson(el.getAsJsonObject()));
        List<Background> backgrounds = new ArrayList<>();
        for (JsonElement el : json.get("background").getAsJsonArray()) {
            JsonObject bg = el.getAsJsonObject();
            ResourceLocation texture = ResourceLocation.tryParse(bg.get("texture").getAsString());
            float speed = Math.max(0, Math.min(1, bg.get("speed").getAsFloat()));
            int sortb = Math.max(0, bg.get("sort").getAsInt());
            backgrounds.add(new Background(sortb, texture, speed));
        }
        ResourceLocation book = ResourceLocation.tryParse(json.get("book").getAsString());
        return new GuideBookCategory(sort, book, Collections.unmodifiableList(backgrounds), title, titleColor, icon, tasks);
    }
    
    public boolean visible(Player player){
        for(Task task : tasks)
            if(!task.get(player)) return false;
        return true;
    }
    
    public record Background(int sort, ResourceLocation texture, float speed) {}
}
