package ru.blatfan.blatapi.common.guide_book;

import net.minecraft.client.gui.GuiGraphics;

public interface BookGuiExtension {
    boolean shouldRender(GuideBookEntry entry);
    default void renderBG(GuiGraphics gui, GuideBookData data, int x, int y){
        gui.blit(data.getTexture(), x, y, 100, 16, 41, 15, 256, 256);
    }
    void render(GuideBookData data, GuideBookEntry entry, GuiGraphics gui, int x, int y, int mX, int mY, float partialTick);
}