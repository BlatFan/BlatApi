package ru.blatfan.blatapi.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import ru.blatfan.blatapi.utils.GuiUtil;

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
    public void render(PoseStack gui, ItemStack stack, int x, int y) {
        TextColor color = TextColor.fromRgb(this.color.getRGB());
        
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor((color.getValue() >> 16 & 255) / 255.0f, (color.getValue() >> 8 & 255) / 255.0f, (color.getValue() & 255) / 255.0f, 1.0f);
        
        GuiUtil.blit(gui, x, y, 16, 16, 0, 0, 16, 16, 16, 16);
    }
}
