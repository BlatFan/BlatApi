package ru.blatfan.blatapi.common.guide_book;

import net.minecraft.client.gui.GuiGraphics;
import ru.blatfan.blatapi.client.guide_book.GuideClient;

public class BookTaskExtension implements BookGuiExtension {
    @Override
    public boolean willRenderBG(GuideBookEntry entry) {
        return !entry.completed(GuideClient.player);
    }
    
    @Override
    public void render(GuideBookEntry entry, GuiGraphics gui, int x, int y, int mX, int mY, float pTick) {
        GuideBookData data = GuideClient.guideBookData;
        if(data==null) return;
        if(entry==null) return;
        if(!entry.completed(GuideClient.player)){
            gui.blit(data.getTexture(), x+10, y, 160, 16, 16, 16, 256, 256);
            if(mX>=x && mX<=x+41 && mY>=y && mY<=y+16){
                gui.pose().pushPose();
                gui.pose().translate(0, 0, 500);
                gui.blitNineSlicedSized(data.getTexture(), mX, mY, 128, 20*entry.tasks().size(), 6, 18, 18, 238, 48, 256, 256);
                for(int i=0; i<entry.tasks().size(); i++)
                    entry.tasks().get(i).render(gui, mX+4, mY+20*i, mX, mY, GuideClient.player);
                gui.pose().popPose();
            }
        }
    }
}
