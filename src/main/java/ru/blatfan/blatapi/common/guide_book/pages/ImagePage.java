package ru.blatfan.blatapi.common.guide_book.pages;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.common.guide_book.GuideBookPage;
import ru.blatfan.blatapi.utils.ColorHelper;
import ru.blatfan.blatapi.utils.GuiUtil;
import ru.blatfan.blatapi.utils.SplitText;
import ru.blatfan.blatapi.utils.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ImagePage extends GuideBookPage {
    public static final ResourceLocation TYPE = BlatApi.loc("image");
    
    private final ResourceLocation image;
    private final int imageWidth;
    private final int imageHeight;
    private final Color color;
    private final SplitText texts;
    
    public ImagePage(Component title, Color titleColor, boolean separator, ResourceLocation image, int imageWidth, int imageHeight, Color color, List<Component> text) {
        super(title, titleColor, separator);
        this.image=image;
        this.imageWidth=imageWidth;
        this.imageHeight=imageHeight;
        this.color = color;
        
        if(!text.isEmpty())
            this.texts = TextPage.splitText(text, GuideClient.pageWidth-12,GuideClient.pageHeight - 10 - this.imageHeight);
        else this.texts=new SplitText(1);
    }
    
    public static GuideBookPage json(JsonObject jsonObject) {
        List<Component> text = new ArrayList<>();
        for(JsonElement element : jsonObject.getAsJsonArray("text"))
            text.add(Text.create(element.getAsString()));
        return new ImagePage(
            Text.create(jsonObject.get("title").getAsString()),
            jsonObject.has("title_color") ? ColorHelper.getColor(jsonObject.get("title_color").getAsString()) : Color.WHITE,
            jsonObject.has("separator") && jsonObject.get("separator").getAsBoolean(),
            ResourceLocation.tryParse(jsonObject.get("image").getAsString()),
            jsonObject.get("image_width").getAsInt(),
            jsonObject.has("image_height") ? jsonObject.get("image_height").getAsInt() : jsonObject.get("image_width").getAsInt(),
            jsonObject.has("color") ? ColorHelper.getColor(jsonObject.get("color").getAsString()) : Color.WHITE, text
        );
    }
    
    @Override
    protected int height() {
        return imageHeight+texts.height();
    }
    
    @Override
    protected void render(GuiGraphics gui, int x, int y, int mX, int mY, float partialTick) {
        Font font = GuideClient.font;
        float sc = (float) (GuideClient.pageWidth-8) /imageWidth;
        GuiUtil.blit(gui, sc, image, x+10, y+1, 0, 0, imageWidth, imageHeight, 256, 256);
        for(int i=0; i<texts.size(); i++){
            String c = texts.get(i);
            int ty = (int) ((imageHeight*sc)+y+2+(font.lineHeight*i));
            GuiUtil.drawScaledString(gui, c, x+4, ty, color, texts.scale());
        }
    }
}