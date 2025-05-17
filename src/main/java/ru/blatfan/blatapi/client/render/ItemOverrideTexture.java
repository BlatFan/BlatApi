package ru.blatfan.blatapi.client.render;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import ru.blatfan.blatapi.client.TOTHandler;

import java.awt.*;
import java.util.List;

public record ItemOverrideTexture(ResourceLocation texture, List<Item> items, Color color) {
    public ItemOverrideTexture(ResourceLocation texture, List<Item> items){
        this(texture, items, Color.WHITE);
    }
    
    public void register(){
        TOTHandler.textures.add(this);
    }
}
