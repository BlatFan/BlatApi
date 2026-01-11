package ru.blatfan.blatapi.client.guide_book.screen;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.common.GuideManager;
import ru.blatfan.blatapi.common.guide_book.BookGuiExtension;
import ru.blatfan.blatapi.common.guide_book.GuideBookData;
import ru.blatfan.blatapi.common.guide_book.GuideBookEntry;
import ru.blatfan.blatapi.common.guide_book.GuideBookPage;

import java.awt.*;

public class GuideBookGui {
    @Getter@Setter
    private GuideBookEntry entry;
    
    private int guiLeft, guiTop;
    private int widthT, heightT;
    private int page = 0;
    private int pages = 0;
    
    @Getter
    private boolean close;
    
    public void render(GuiGraphics gui, int mX, int mY, float partialTick) {
        if(entry==null) return;
        widthT = 272;
        heightT = 181;
        guiLeft = (gui.guiWidth()-widthT)/2;
        guiTop = (gui.guiHeight()-heightT)/2;
        pages=entry.pages().size()-1;
        Font font = GuideClient.font;
        GuideBookData data = GuideClient.guideBookData;
        page = Math.max(0, Math.min(pages-1, page));
        renderBackground(gui);
        
        GuideBookPage page1 = entry.pages().get(page);
        page1.renderPage(gui, guiLeft+6, guiTop+6, mX, mY, partialTick);
        if(page+1<=pages){
            GuideBookPage page2 = entry.pages().get(page+1);
            page2.renderPage(gui, guiLeft+131, guiTop+6, mX, mY, partialTick);
        }
        gui.drawString(font, String.valueOf(page+1), guiLeft+11, guiTop+161, Color.WHITE.getRGB());
        if(hasNext(page)) gui.drawString(font, String.valueOf(page+2), guiLeft+251, guiTop+161, Color.WHITE.getRGB());
        
        int x = guiLeft+11;
        int y = guiTop+175;
        if(hasPrevious(page)){
            boolean b = mX>=x && mX<=x+18 && mY>=y && mY<=y+10;
            gui.blit(data.getTexture(), x, y, 51, 52+(b?10:0), 18, 10);
        }
        x+=240;
        if(hasNext(page+1)){
            boolean b = mX>=x && mX<=x+18 && mY>=y && mY<=y+10;
            gui.blit(data.getTexture(), x, y, 33, 52+(b?10:0), 18, 10);
        }
        if(page>2){
            y += hasNext(page+1)?25:0;
            boolean b = mX>=x && mX<=x+18 && mY>=y && mY<=y+9;
            gui.blit(data.getTexture(), x, y, 82, 52+(b?9:0), 18, 9);
        }
        x = guiLeft+GuideClient.pageWidth+8;
        y = guiTop+185;
        boolean b = mX>=x && mX<=x+11 && mY>=y && mY<=y+12;
        gui.blit(data.getTexture(), x, y, 70, 52+(b?12:0), 11, 12);
        
        for(int i=0; i<GuideManager.getBookExtensions().size(); i++) {
            BookGuiExtension ex = GuideManager.getBookExtensions().values().toArray(new BookGuiExtension[0])[i];
            if(ex.shouldRender(getEntry()))
                ex.render(data, getEntry(), gui, guiLeft + 262, guiTop + 8 + (i*18), mX, mY, partialTick);
        }
    }
    
    private boolean hasNext(int p){
        return p+1<=pages;
    }
    private boolean hasPrevious(int p){
        return p-1>=0;
    }
    
    public void renderBackground(GuiGraphics gui) {
        if(entry==null) return;
        GuideBookData data = GuideClient.guideBookData;
        gui.fillGradient(0, 0, gui.guiWidth(), gui.guiHeight(), -1072689136, -804253680);
        gui.blitNineSlicedSized(data.getTexture(), guiLeft, guiTop, widthT, heightT, 16, 48, 48, 0, 0, 256, 256);
        gui.blit(data.getTexture(), guiLeft+8, guiTop+8, 0, 91, 256, 165, 256, 256);
    }
    
    private int scrollBack(){
        if(hasPrevious(page+2)) return -2;
        return -1;
    }
    
    private int scrollUp(){
        if(hasNext(page+2)) return 2;
        return 1;
    }
    
    public boolean mouseClicked(double mX, double mY, int button) {
        if(entry==null) return false;
        int x = guiLeft+11;
        int y = guiTop+175;
        if(hasPrevious(page) && mX>=x && mX<=x+18 && mY>=y && mY<=y+10 && button==0){
            page+=scrollBack();
            return true;
        }
        x+=240;
        if(hasNext(page+1) && mX>=x && mX<=x+18 && mY>=y && mY<=y+10 && button==0){
            page+=scrollUp();
            return true;
        }
        y += hasNext(page+1)?25:0;
        if(page>3 && mX>=x && mX<=x+18 && mY>=y && mY<=y+9 && button==0){
            page=0;
            return true;
        }
        x = guiLeft+GuideClient.pageWidth+8;
        y = guiTop+185;
        if(mX>=x && mX<=x+11 && mY>=y && mY<=y+12 && button==0){
            close = true;
            return true;
        }
        if(!(mX>=guiLeft && mX<=guiLeft+widthT &&
            mY>=guiTop && mY<=guiTop+heightT)) {
            close = true;
            return true;
        }
        if(page+1<=pages){
            GuideBookPage page2 = entry.pages().get(page+1);
            return page2.mouseClicked(mX, mY, button);
        }
        GuideBookPage page1 = entry.pages().get(page);
        return page1.mouseClicked(mX, mY, button);
    }
    
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if(entry==null) return false;
        page = page+(delta<0?2:-2);
        return true;
    }
    
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return false;
    }
}