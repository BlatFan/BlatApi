package ru.blatfan.blatapi.common.guide_book;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import ru.blatfan.blatapi.common.task.Task;

import java.util.ArrayList;
import java.util.List;

public record GuideBookPaper(ResourceLocation item, ResourceLocation entry, ResourceLocation book, List<Task> tasks) {
    public static GuideBookPaper json(JsonElement element){
        JsonObject json = element.getAsJsonObject();
        ResourceLocation item = ResourceLocation.tryParse(json.get("item").getAsString());
        ResourceLocation entry = ResourceLocation.tryParse(json.get("entry").getAsString());
        ResourceLocation book = ResourceLocation.tryParse(json.get("book").getAsString());
        List<Task> tasks = new ArrayList<>();
        if(json.has("tasks"))
            for (JsonElement el : json.get("tasks").getAsJsonArray())
                tasks.add(Task.fromJson(el.getAsJsonObject()));
        return new GuideBookPaper(item, entry, book, tasks);
    }
    
    public ItemStack getItemStack(){
        Item item1 = ForgeRegistries.ITEMS.getValue(item);
        if(item1 instanceof GuidePaperItem paperItem){
            return GuidePaperItem.getPaper(paperItem, entry, book, tasks);
        } else
            throw new IllegalArgumentException("Item in GuideBookPaper must be instance of GuidePaperItem");
    }
    
    public boolean completed(Player player){
        if(tasks.isEmpty()) return true;
        for(Task task : tasks) if(!task.get(player)) return false;
        return true;
    }
}