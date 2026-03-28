package ru.blatfan.blatapi.client.gui.screen;

import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@Getter
public class BAPanorama {
    public String id;
    public Component name;
    public ResourceLocation texture = ResourceLocation.parse("textures/gui/title/background/panorama");
    public boolean flat = false;
    public ResourceLocation logo;
    public ItemStack itemStack = new ItemStack(Items.DIRT);
    public BlatMod mod;
    public int sort = 0;

    public BAPanorama(String id, Component name) {
        this.id = id;
        this.name = name;
    }

    public BAPanorama setTexture(ResourceLocation texture) {
        this.texture = texture;
        return this;
    }

    public BAPanorama setLogo(ResourceLocation logo) {
        this.logo = logo;
        return this;
    }

    public BAPanorama setItem(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }
    
    public BAPanorama setMod(BlatMod mod) {
        this.mod = mod;
        return this;
    }
    
    public BAPanorama setFlat(boolean value) {
        this.flat = value;
        return this;
    }

    public BAPanorama setSort(int sort) {
        this.sort = sort;
        return this;
    }
}
