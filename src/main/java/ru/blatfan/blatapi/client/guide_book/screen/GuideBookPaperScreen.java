package ru.blatfan.blatapi.client.guide_book.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.common.GuideManager;
import ru.blatfan.blatapi.common.guide_book.GuideBookData;
import ru.blatfan.blatapi.common.guide_book.GuideBookEntry;

import java.awt.*;
import java.util.ArrayList;

public class GuideBookPaperScreen extends Screen {
    private final ResourceLocation entry;
    private final ResourceLocation book;
    private GuideBookGui bookGui;
    
    public GuideBookPaperScreen(ResourceLocation entry, ResourceLocation book) {
        super(Component.empty());
        this.entry = entry;
        this.book = book;
    }
    
    @Override
    protected void init() {
        GuideBookEntry e = GuideManager.getEntry(entry);
        GuideBookEntry bookEntry = new GuideBookEntry(
            e.title(), e.description(), e.icon(), e.category(), e.x(), e.y(), e.pages(),
            new ArrayList<>(), new ArrayList<>(), e.advance()
        );
        bookGui = new GuideBookGui(){
            @Override
            public void renderBackground(GuiGraphics gui) {
                int guiLeft = (gui.guiWidth() - 272) / 2;
                int guiTop = (gui.guiHeight() - 181) / 2;
                GuideBookData data = GuideClient.guideBookData;
                gui.blit(data.getTexture(), guiLeft + 8, guiTop + 8, 130, 91, 126, 165, 256, 256);
                if(getEntry().pages().size()>1)
                    gui.blit(data.getTexture(), guiLeft + 8 + 128, guiTop + 8, 130, 91, 126, 165, 256, 256);
                if(isClose())
                    gui.drawString(font, "TO CLOSE", 0, 0, Color.WHITE.getRGB());
            }
        };
        bookGui.setEntry(bookEntry);
    }
    
    @Override
    public void render(GuiGraphics gui, int mX, int mY, float partialTick) {
        GuideBookData lastGBD = GuideClient.guideBookData;
        GuideClient.guideBookData=GuideManager.getBook(book);
        if(GuideClient.guideBookData!=null && bookGui!=null && bookGui.getEntry()!=null) {
            renderBackground(gui);
            bookGui.render(gui, mX, mY, partialTick);
        } else onClose();
        if(bookGui!=null && bookGui.isClose()) onClose();
        GuideClient.guideBookData=lastGBD;
    }
    
    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if(bookGui!=null && bookGui.getEntry()!=null)
            return bookGui.mouseClicked(pMouseX, pMouseY, pButton);
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
    
    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if(bookGui!=null && bookGui.getEntry()!=null)
            return bookGui.mouseScrolled(pMouseX, pMouseY, pDelta);
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }
    
    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if(bookGui!=null && bookGui.getEntry()!=null)
            return bookGui.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}