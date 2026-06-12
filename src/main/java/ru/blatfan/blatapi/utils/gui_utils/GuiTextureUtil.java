package ru.blatfan.blatapi.utils.gui_utils;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

@UtilityClass
@SuppressWarnings("ALL")
public class GuiTextureUtil {
    public static TextureAtlasSprite getSprite(ResourceLocation resourceLocation) {
        try {
            return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(resourceLocation);
        } catch (Exception e) {
            return getMissingTexture();
        }
    }
    public static TextureAtlasSprite getMissingTexture() {
        try {
            return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(ResourceLocation.fromNamespaceAndPath("minecraft", "missingno"));
        } catch (Exception e) {
            return null;
        }
    }
    
    public static TextureAtlasSprite getSprite(String modId, String sprite) {
        return getSprite(ResourceLocation.fromNamespaceAndPath(modId, sprite));
    }
}
