package ru.blatfan.blatapi.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.List;

public class ItemOverrideTexture extends IItemOverrideRenderer {
    protected final ResourceLocation texture;
    protected final Color color;
    
    public ItemOverrideTexture(ResourceLocation texture, List<Item> items, Color color){
        super(items);
        this.texture=texture;
        this.color=color;
    }
    
    public ItemOverrideTexture(ResourceLocation texture, List<Item> items){
        this(texture, items, Color.WHITE);
    }
    
    @Override
    public void render(GuiGraphics gui, ItemStack stack, int x, int y) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
        
        gui.blit(texture, x, y, 16, 16, 0, 0, 16, 16, 16, 16);
    }
}
