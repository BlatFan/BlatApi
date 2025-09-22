package ru.blatfan.blatapi.common.guide_book;

import net.minecraft.client.gui.GuiGraphics;

public interface BookGuiExtension {
    boolean willRenderBG(GuideBookEntry entry);
    void render(GuideBookEntry entry, GuiGraphics gui, int guiLeft, int guiTop, int mX, int mY, float partialTick);
}