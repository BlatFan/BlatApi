package ru.blatfan.blatapi.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public abstract class BlatOverlay implements IGuiOverlay {
    private static final int HOTBAR_HEIGHT = 32;
    private static final int SCREEN_BORDER_MARGIN = 20;
    private final int DEFAULT_IMAGE_WIDTH, IMAGE_HEIGHT;
    
    protected BlatOverlay(int defaultImageWidth, int imageHeight) {
        DEFAULT_IMAGE_WIDTH = defaultImageWidth;
        IMAGE_HEIGHT = imageHeight;
    }
    
    public int getBarX(Anchor anchor, int screenWidth) {
        if (anchor == Anchor.Center)
            return screenWidth / 2 - DEFAULT_IMAGE_WIDTH / 2;
        else if (anchor == Anchor.TopLeft || anchor == Anchor.BottomLeft)
            return SCREEN_BORDER_MARGIN;
        else return screenWidth - SCREEN_BORDER_MARGIN - DEFAULT_IMAGE_WIDTH;
    }
    
    public int getBarY(Anchor anchor, int screenHeight) {
        if (anchor == Anchor.Center)
            return screenHeight - HOTBAR_HEIGHT - IMAGE_HEIGHT / 2;
        if (anchor == Anchor.TopLeft || anchor == Anchor.TopRight)
            return SCREEN_BORDER_MARGIN;
        return screenHeight - SCREEN_BORDER_MARGIN - IMAGE_HEIGHT;
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