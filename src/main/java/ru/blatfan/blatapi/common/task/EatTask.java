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
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;
import ru.blatfan.blatapi.BlatApi;
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
    
    @Override
    public ResourceLocation getType() {
        return BlatApi.loc("eat");
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
    
    public static class Serializer implements ITaskSerializer {
        public static final Serializer INSTANCE = new Serializer();
        protected Serializer(){}
        @Override
        public Task fromJson(JsonObject json) {
            boolean b = !json.has("visible") || json.get("visible").getAsBoolean();
            ItemStack item = CraftingHelper.getItemStack(json.get("food").getAsJsonObject(), true);
            PlayerStages.allStages.add(getStage(item.getItem()));
            return new EatTask(b, item);
        }
        
        @Override
        public Task fromNBT(CompoundTag tag) {
            boolean b = !tag.contains("visible") || tag.getBoolean("visible");
            ItemStack item = ItemStack.of(tag.getCompound("food"));
            PlayerStages.allStages.add(getStage(item.getItem()));
            return new EatTask(b, item);
        }
        
        @Override
        public CompoundTag toNBT(Task task) {
            CompoundTag tag = new CompoundTag();
            if(task instanceof EatTask task1){
                tag.putBoolean("visible", task1.visible);
                tag.put("food", task1.food.serializeNBT());
            }
            return tag;
        }
    }
}