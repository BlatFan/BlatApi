package ru.blatfan.blatapi.common.guide_book.pages;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.common.guide_book.GuideBookPage;
import ru.blatfan.blatapi.utils.ColorHelper;
import ru.blatfan.blatapi.utils.GuiUtil;
import ru.blatfan.blatapi.utils.collection.SplitText;
import ru.blatfan.blatapi.utils.collection.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class TextPage extends GuideBookPage {
    public static final ResourceLocation TYPE = BlatApi.loc("text");
    
    private final Color color;
    private final SplitText texts;
    
    public TextPage(Component title, Color titleColor, boolean separator, List<Component> text, Color color) {
        super(title, titleColor, separator);
        this.color = color;
        
        this.texts = splitText(text, GuideClient.pageWidth-12,GuideClient.pageHeight - 10);
    }
    public static GuideBookPage json(JsonObject jsonObject) {
        List<Component> text = new ArrayList<>();
        for(JsonElement element : jsonObject.getAsJsonArray("text"))
            text.add(Text.create(element.getAsString()));
        return new TextPage(
            Text.create(jsonObject.get("title").getAsString()),
            jsonObject.has("title_color") ? ColorHelper.getColor(jsonObject.get("title_color").getAsString()) : Color.WHITE,
            jsonObject.has("separator") && jsonObject.get("separator").getAsBoolean(),
            text, jsonObject.has("color") ? ColorHelper.getColor(jsonObject.get("color").getAsString()) : Color.WHITE
        );
    }
    
    public static SplitText splitText(List<Component> text, int width, int height){
        return GuiUtil.splitText(text, width, GuiUtil.findScale(text, width, height));
    }
    
    // TODO in 0.3.5
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = ">0.3.4")
    public static float findOptimalScale(List<Component> text, int width, int height) {
        return GuiUtil.findScale(text, width, height);
    }
    
    @Override
    public int height() {
        return texts.height();
    }
    
    @Override
    public void render(GuiGraphics gui, int x, int y, int mX, int mY, float partialTick) {
        Font font = GuideClient.font;
        for(int i=0; i<texts.size(); i++){
            String t = texts.get(i);
            int ty = y+(int)(font.lineHeight*i*texts.scale());
            GuiUtil.drawScaledString(gui, t, x+8, ty, color, texts.scale());
        }
    }
}
