package ru.blatfan.blatapi.fluffy_fur.client.gui.screen;

import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@Getter
public class FluffyFurPanorama {
    public String id;
    public Component name;
    public ResourceLocation texture = new ResourceLocation("textures/gui/title/background/panorama");
    public ResourceLocation logo;
    public ItemStack itemStack = new ItemStack(Items.DIRT);
    public FluffyFurMod mod;
    public int sort = 0;

    public FluffyFurPanorama(String id, Component name) {
        this.id = id;
        this.name = name;
    }

    public FluffyFurPanorama setTexture(ResourceLocation texture) {
        this.texture = texture;
        return this;
    }

    public FluffyFurPanorama setLogo(ResourceLocation logo) {
        this.logo = logo;
        return this;
    }

    public FluffyFurPanorama setItem(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    public FluffyFurPanorama setMod(FluffyFurMod mod) {
        this.mod = mod;
        return this;
    }

    public FluffyFurPanorama setSort(int sort) {
        this.sort = sort;
        return this;
    }
}
