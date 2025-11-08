package ru.blatfan.blatapi.common.task;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;
import ru.blatfan.blatapi.utils.collection.Text;

import java.awt.*;

@AllArgsConstructor
@Getter
public class ItemTask extends Task {
    private boolean visible;
    private int amount;
    private ItemStack item;
    
    @Override
    public boolean get(Player player) {
        int pamount=0;
        for(ItemStack itemStack : player.getInventory().items)
            if(itemStack.is(item.getItem())) pamount+=itemStack.getCount();
        return pamount>=amount;
    }
    
    public static ItemTask fromJson(JsonObject json){
        boolean b = !json.has("visible") || json.get("visible").getAsBoolean();
        int a = !json.has("amount") ? 1 : json.get("amount").getAsInt();
        ItemStack item = CraftingHelper.getItemStack(json.get("item").getAsJsonObject(), true);
        return new ItemTask(b, a, item);
    }
    
    @Override
    public Component text(Player player) {
        return Text.create(item.getHoverName()).add(" x"+amount).withColor(Color.WHITE);
    }
    
    @Override
    public void render(GuiGraphics gui, int x, int y, int mX, int mY, Player player) {
        gui.renderItem(item, x, y);
        gui.renderItemDecorations(Minecraft.getInstance().font, item, x, y);
        super.render(gui, x+24, y, mX, mY, player);
    }
}