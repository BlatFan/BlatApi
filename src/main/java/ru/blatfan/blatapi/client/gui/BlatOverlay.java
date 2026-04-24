package ru.blatfan.blatapi.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

@SuppressWarnings("unused")
public abstract class BlatOverlay implements IGuiOverlay {
    protected static final int HOTBAR_HEIGHT = 32;
    protected static final int SCREEN_BORDER_MARGIN = 20;
    protected int defaultImageWidth, imageHeight;
    
    protected BlatOverlay(int defaultImageWidth, int imageHeight) {
        this.defaultImageWidth = defaultImageWidth;
        this.imageHeight = imageHeight;
    }
    
    public int getBarX(Anchor anchor, int screenWidth) {
        if (anchor == Anchor.Center)
            return screenWidth / 2 - defaultImageWidth / 2;
        else if (anchor == Anchor.TopLeft || anchor == Anchor.BottomLeft)
            return SCREEN_BORDER_MARGIN;
        else return screenWidth - SCREEN_BORDER_MARGIN - defaultImageWidth;
    }
    
    public int getBarY(Anchor anchor, int screenHeight) {
        if (anchor == Anchor.Center)
            return screenHeight - HOTBAR_HEIGHT - imageHeight / 2;
        if (anchor == Anchor.TopLeft || anchor == Anchor.TopRight)
            return SCREEN_BORDER_MARGIN;
        return screenHeight - SCREEN_BORDER_MARGIN - imageHeight;
    }
    
    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        render(screenWidth, screenHeight, forgeGui, guiGraphics, partialTick);
        render(screenWidth, screenHeight, guiGraphics, partialTick);
        render(screenWidth, screenHeight, guiGraphics);
    }
    
    public void render(int width, int height, ForgeGui forgeGui, GuiGraphics guiGraphics, float partialTick){}
    public void render(int width, int height, GuiGraphics gui, float partialTick){}
    public void render(int width, int height, GuiGraphics gui){}
    public enum Anchor {
        Center,
        TopLeft,
        TopRight,
        BottomLeft,
        BottomRight
    }
}