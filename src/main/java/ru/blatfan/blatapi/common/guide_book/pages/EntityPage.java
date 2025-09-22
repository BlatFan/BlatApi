package ru.blatfan.blatapi.common.guide_book.pages;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.common.guide_book.GuideBookPage;
import ru.blatfan.blatapi.utils.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EntityPage extends GuideBookPage {
    public static final ResourceLocation TYPE = BlatApi.loc("entity");
    
    private final ResourceLocation entity;
    private final int scale;
    private final int offsetX, offsetY;
    private final Color color;
    private final SplitText texts;
    
    public EntityPage(Component title, Color titleColor, boolean separator, ResourceLocation entity, int scale, int offsetX, int offsetY, Color color, java.util.List<net.minecraft.network.chat.Component> text) {
        super(title, titleColor, separator);
        this.entity=entity;
        this.scale = scale;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.color = color;
        
        if(!text.isEmpty()) this.texts = TextPage.splitText(text, GuideClient.pageWidth-4,GuideClient.pageHeight - 100);
        else this.texts=new SplitText(1);
    }
    
    public static GuideBookPage json(JsonObject jsonObject) {
        List<Component> text = new ArrayList<>();
        if(jsonObject.has("text"))
            for(JsonElement element : jsonObject.getAsJsonArray("text"))
                text.add(Text.create(element.getAsString()));
        return new EntityPage(
            Text.create(jsonObject.get("title").getAsString()),
            jsonObject.has("title_color") ? ColorHelper.getColor(jsonObject.get("title_color").getAsString()) : Color.WHITE,
            jsonObject.has("separator") && jsonObject.get("separator").getAsBoolean(),
            ResourceLocation.tryParse(jsonObject.get("entity").getAsString()),
            jsonObject.get("scale").getAsInt(),
            jsonObject.has("offsetX") ? jsonObject.get("offsetX").getAsInt() : 0,
            jsonObject.has("offsetY") ? jsonObject.get("offsetY").getAsInt() : 0,
            jsonObject.has("color") ? ColorHelper.getColor(jsonObject.get("color").getAsString()) : Color.WHITE, text
        );
    }
    
    @Override
    protected int height() {
        return offsetY+(texts.height());
    }
    
    @Override
    protected void render(GuiGraphics gui, int x, int y, int mX, int mY, float partialTick) {
        Font font = GuideClient.font;
        Entity e = BuiltInRegistries.ENTITY_TYPE.get(entity).create(GuideClient.level);
        if(!(e instanceof LivingEntity living)) return;
        living.yHeadRot=0;
        living.yHeadRotO=0;
        GuiUtil.renderEntityQuaternionf(gui, x+5+offsetX, y+offsetY, scale, Axis.YP.rotationDegrees(ClientTicks.ticks),
            (Axis.XP.rotationDegrees(180)), living);
        gui.fill(x+4, y, x+4, y+10, 0xffffff);
        for(int i=0; i<texts.size(); i++){
            String c = texts.get(i);
            int ty = offsetY+y+2+(font.lineHeight*i);
            GuiUtil.drawScaledString(gui, c, x+4, ty, color, texts.scale());
        }
    }
}
