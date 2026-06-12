package ru.blatfan.blatapi.client.render;

import lombok.Getter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import ru.blatfan.blatapi.client.TOTHandler;

import java.util.List;

public abstract class IItemOverrideRenderer {
    @Getter
    protected final List<Item> items;
    
    protected IItemOverrideRenderer(List<Item> items) {
        this.items = items;
    }
    
    public void register(){
        TOTHandler.renderers.add(this);
    }
    
    public abstract void render(GuiGraphics gui, ItemStack stack, int x, int y);
}