package ru.blatfan.blatapi.client.render;

import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import ru.blatfan.blatapi.client.TOTHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ItemOverrideTexture{
    private final ResourceLocation texture;
    private final List<Item> items = new ArrayList<>();
    private final Color color;
    
    public ItemOverrideTexture(ResourceLocation texture, List<Item> items, Color color){
        this.texture = texture;
        this.items.addAll(items);
        this.color = color;
    }
    public ItemOverrideTexture(ResourceLocation texture, List<Item> items){
        this(texture, items, Color.WHITE);
    }
    
    public void register(){
        TOTHandler.textures.add(this);
    }
}
