package ru.blatfan.blatapi.common.guide_book;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.common.guide_book.pages.EmptyPage;
import ru.blatfan.blatapi.utils.RecipeUtil;
import ru.blatfan.blatapi.utils.collection.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Getter
public abstract class GuideBookPage {
    private static final Map<ResourceLocation, Function<JsonObject, GuideBookPage>> TYPES = new HashMap<>();
    private final Component title;
    private final Color titleColor;
    private final boolean separator;
    
    private final List<RenderItem> renderItems = new ArrayList<>();
    
    public static void addPageType(ResourceLocation type, Function<JsonObject, GuideBookPage> deserialize){
        if(!TYPES.containsKey(type))
            TYPES.put(type, deserialize);
    }
    
    public GuideBookPage(Component title, Color titleColor, boolean separator) {
        this.title = title;
        this.titleColor = titleColor;
        this.separator = separator;
    }
    
    public int getHeight(){
        return height()+(!renderItems.isEmpty() ? renderItems.size() % 5 * 18 : 0);
    }
    protected abstract int height();
    
    public void renderPage(GuiGraphics gui, int x, int y, int mX, int mY, float partialTick){
        Font font = GuideClient.font;
        if(separator) {
            gui.setColor(1, 1, 1, 0.7f);
            int xs = (GuideClient.pageWidth/2)-font.width(getTitle())/2-4;
            gui.blitNineSlicedSized(GuideClient.guideBookData.getTexture(), x+8, y + (font.lineHeight / 2), xs-8, 4,
                8, 24, 4, 144, 32, 256, 256);
            gui.blitNineSlicedSized(GuideClient.guideBookData.getTexture(), x+8+xs+font.width(getTitle()), y + (font.lineHeight / 2), xs, 4,
                8, 24, 4, 144, 32, 256, 256);
            gui.setColor(1, 1, 1, 1);
        }
        gui.drawCenteredString(font, getTitle(), x+(GuideClient.pageWidth/2), y, getTitleColor().getRGB());
        
        render(gui, x, y+ font.lineHeight+2, mX, mY, partialTick);
        
        for (int i = 0; i < renderItems.size(); i++) {
            int column = i % 7;
            int row = i / 7;
            if (height() + row * 18 > GuideClient.pageHeight) return;
            renderItems.get(i).render(gui, x + 4 + column * 18, y + height() + row * 18, mX, mY);
        }
    }
    protected abstract void render(GuiGraphics gui, int x, int y, int mX, int mY, float partialTick);
    public boolean mouseClicked(double mX, double mY, int button){
        return false;
    }
    
    public static GuideBookPage json(JsonElement element){
        JsonObject json = element.getAsJsonObject();
        ResourceLocation type = ResourceLocation.tryParse(json.get("type").getAsString());
        GuideBookPage page = TYPES.containsKey(type) ? TYPES.get(type).apply(json) : new EmptyPage();
        if(json.has("item_page"))
            for(JsonElement el : json.get("item_page").getAsJsonArray())
                page.renderItems.add(RenderItem.deserialize(el));
        return page;
    }
    
    private record RenderItem(ItemStack stack, boolean pedestal, List<Component> tooltips){
        public static RenderItem deserialize(JsonElement element){
            JsonObject json = element.getAsJsonObject();
            ItemStack stack = RecipeUtil.itemStackFromJson(json);
            List<Component> tooltips = new ArrayList<>();
            if(json.has("tooltips")) for(JsonElement el : json.get("tooltips").getAsJsonArray()) tooltips.add(Text.create(el.getAsString()));
            boolean pedestal = !json.has("pedestal") || json.get("pedestal").getAsBoolean();
            
            return new RenderItem(stack, pedestal, tooltips);
        }
        
        public void render(GuiGraphics gui, int x, int y, int mX, int mY){
            Font font = GuideClient.mc.font;
            if(pedestal){
                gui.pose().pushPose();
                gui.pose().translate(0, 0, -50);
                gui.renderFakeItem(GuideClient.guideBookData.getPedestal(), x, y+6);
                gui.pose().popPose();
            }
            gui.renderItem(stack, x, y);
            gui.renderItemDecorations(font, stack, x, y);
            if(mX>=x && mX<=x+16 && mY>=y && mY<=y+16) {
                List<Component> s = new ArrayList<>(tooltips);
                s.removeIf(c -> c.getString().equals("item_tooltips"));
                
                gui.renderComponentTooltip(font, getTooltipLines(stack, s, GuideClient.player, GuideClient.tooltipFlag), mX, mY);
            }
        }
        
        private List<Component> getTooltipLines(ItemStack stack, List<Component> addition, Player pPlayer, TooltipFlag pIsAdvanced) {
            List<Component> list = stack.getTooltipLines(pPlayer, pIsAdvanced);
            list.addAll(1, addition);
            return list;
        }
    }
}