package ru.blatfan.blatapi.common.task;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.utils.collection.Text;

import java.awt.*;

@AllArgsConstructor
@Getter
public class HasItemTask extends Task {
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
    
    @Override
    public ResourceLocation getType() {
        return BlatApi.loc("has_item");
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
    
    public static class Serializer implements ITaskSerializer {
        public static final Serializer INSTANCE = new Serializer();
        protected Serializer(){}
        @Override
        public Task fromJson(JsonObject json) {
            boolean b = !json.has("visible") || json.get("visible").getAsBoolean();
            int a = !json.has("amount") ? 1 : json.get("amount").getAsInt();
            ItemStack item = CraftingHelper.getItemStack(json.get("item").getAsJsonObject(), true);
            return new HasItemTask(b, a, item);
        }
        
        @Override
        public Task fromNBT(CompoundTag tag) {
            boolean b = !tag.contains("visible") || tag.getBoolean("visible");
            int a = !tag.contains("amount") ? 1 : tag.getInt("amount");
            CompoundTag itemTag = tag.getCompound("item");
            ItemStack item = ItemStack.of(itemTag);
            return new HasItemTask(b, a, item);
        }
        
        @Override
        public CompoundTag toNBT(Task task) {
            CompoundTag tag = new CompoundTag();
            if(task instanceof HasItemTask task1){
                tag.putBoolean("visible", task1.visible);
                tag.putInt("amount", task1.amount);
                tag.put("item", task1.item.serializeNBT());
            }
            return tag;
        }
    }
}