package ru.blatfan.blatapi.common.guide_book;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import ru.blatfan.blatapi.common.task.Task;
import ru.blatfan.blatapi.utils.RecipeUtil;
import ru.blatfan.blatapi.utils.collection.Text;

import java.util.ArrayList;
import java.util.List;

public record GuideBookEntry(Component title, Component description, ItemStack icon, ResourceLocation category, int x, int y, List<GuideBookPage> pages,
                             List<Task> tasks, List<Task> view, boolean advance) {
    public static GuideBookEntry json(JsonElement element){
        JsonObject json = element.getAsJsonObject();
        Component title = Text.create(json.get("title").getAsString());
        Component description = Text.create(json.get("description").getAsString());
        ItemStack icon = RecipeUtil.itemStackFromJson(json.get("icon").getAsJsonObject());
        ResourceLocation category = ResourceLocation.tryParse(json.get("category").getAsString());
        int x = json.get("x").getAsInt() * 26;
        int y = -json.get("y").getAsInt() * 26;
        boolean adv = json.has("advance") && json.get("advance").getAsBoolean();
        List<GuideBookPage> pages = new ArrayList<>();
        for(JsonElement el : json.get("pages").getAsJsonArray()) pages.add(GuideBookPage.json(el));
        List<Task> tasks = new ArrayList<>();
        if(json.has("tasks"))
            for (JsonElement el : json.get("tasks").getAsJsonArray())
                tasks.add(Task.fromJson(el.getAsJsonObject()));
        List<Task> view = new ArrayList<>();
        if(json.has("view"))
            for (JsonElement el : json.get("view").getAsJsonArray())
                view.add(Task.fromJson(el.getAsJsonObject()));
        return new GuideBookEntry(title, description, icon, category, x, y, pages, tasks, view, adv);
    }
    
    public boolean visible(Player player){
        if(view.isEmpty()) return true;
        for(Task task : view) if(!task.get(player)) return false;
        return true;
    }
    
    public boolean completed(Player player){
        if(tasks.isEmpty()) return true;
        for(Task task : tasks) if(!task.get(player)) return false;
        return true;
    }
}