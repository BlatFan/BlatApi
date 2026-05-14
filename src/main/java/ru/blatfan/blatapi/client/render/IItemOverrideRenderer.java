package ru.blatfan.blatapi.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
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
        TOTHandler.textures.add(this);
    }
    
    public abstract void render(PoseStack gui, ItemStack stack, int x, int y);
}