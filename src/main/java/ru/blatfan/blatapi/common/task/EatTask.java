package ru.blatfan.blatapi.common.task;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;
import ru.blatfan.blatapi.utils.collection.Text;

import java.awt.*;

@AllArgsConstructor
@Getter
public class EatTask extends Task {
    private boolean visible;
    private ItemStack food;
    
    public static String getStage(ItemLike item){
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item.asItem());
        if(id==null) return "EMPTY";
        return "player_eat_"+id.getNamespace()+"_"+id.getPath();
    }
    
    @Override
    public boolean get(Player player) {
        return PlayerStages.getBool(player, getStage(food.getItem()));
    }
    
    public static EatTask fromJson(JsonObject json){
        boolean b = !json.has("visible") || json.get("visible").getAsBoolean();
        ItemStack item = CraftingHelper.getItemStack(json.get("food").getAsJsonObject(), true);
        PlayerStages.allStages.add(getStage(item.getItem()));
        return new EatTask(b, item);
    }
    
    @Override
    public Component text(Player player) {
        return Text.create("task.blatapi.eat").add(food.getHoverName()).withColor(Color.WHITE);
    }
    
    @Override
    public void render(GuiGraphics gui, int x, int y, int mX, int mY, Player player) {
        gui.renderItem(food, x, y);
        gui.renderItemDecorations(Minecraft.getInstance().font, food, x, y);
        super.render(gui, x+24, y, mX, mY, player);
    }
}