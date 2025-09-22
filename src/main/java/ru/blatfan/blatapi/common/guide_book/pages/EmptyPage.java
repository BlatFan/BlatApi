package ru.blatfan.blatapi.common.guide_book.pages;

import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.common.guide_book.GuideBookPage;

import java.awt.*;

public class EmptyPage extends GuideBookPage {
    public static final ResourceLocation TYPE = BlatApi.loc("empty");
    public EmptyPage() {
        super(Component.empty(), Color.RED, false);
    }
    
    public static GuideBookPage json(JsonObject json){
        return new EmptyPage();
    }
    
    @Override
    public int height() {
        return 0;
    }
    
    @Override
    public void render(GuiGraphics gui, int x, int y, int mX, int mY, float partialTick) {}
}
