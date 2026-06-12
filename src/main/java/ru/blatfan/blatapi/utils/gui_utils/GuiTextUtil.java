package ru.blatfan.blatapi.utils.gui_utils;

import com.mojang.blaze3d.vertex.PoseStack;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import ru.blatfan.blatapi.utils.collection.SplitText;
import ru.blatfan.blatapi.utils.collection.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static ru.blatfan.blatapi.utils.gui_utils.GuiRenderUtil.FULL_BRIGHT;

@UtilityClass
@SuppressWarnings("ALL")
public class GuiTextUtil {
    public static Font getFont(){
        return Minecraft.getInstance().font;
    }
    
    public static void drawScaledString(GuiGraphics gui, Font font, Component text, float x, float y, Color color, float size){
        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.scale(size, size, 1);
        gui.drawString(font, text.getVisualOrderText(), x / size, y / size, color.getRGB(), true);
        pose.popPose();
    }
    public static void drawScaledString(GuiGraphics gui, String text, float x, float y, Color color, float size){
        drawScaledString(gui, getFont(), Component.literal(text), x, y, color, size);
    }
    public static void drawScaledString(GuiGraphics gui, Font font, String text, float x, float y, Color color, float size){
        drawScaledString(gui, font, Component.literal(text), x, y, color, size);
    }
    public static void drawScaledString(GuiGraphics gui, Component text, float x, float y, Color color, float size){
        drawScaledString(gui, getFont(), text, x, y, color, size);
    }
    
    public static void drawScaledCentreString(GuiGraphics gui, String text, float x, float y, Color color, float size){
        drawScaledCentreString(gui, getFont(), text, x, y, color, size);
    }
    public static void drawScaledCentreString(GuiGraphics gui, Font font, String text, float x, float y, Color color, float size){
        drawScaledCentreString(gui, font, Component.literal(text), x, y, color, size);
    }
    public static void drawScaledCentreString(GuiGraphics gui, Component text, float x, float y, Color color, float size){
        drawScaledCentreString(gui, getFont(), text, x, y, color, size);
    }
    public static void drawScaledCentreString(GuiGraphics gui, Font font, Component text, float x, float y, Color color, float size){
        drawScaledString(gui, font, text, x-font.width(text)*size/2, y, color, size);
    }
    
    public static int drawString(GuiGraphics gui, Font font, @Nullable Component component, float x, float y, int color, boolean drawShadow) {
        if (component == null) return 0;
        else {
            int i = font.drawInBatch(component, x, y, color, drawShadow, gui.pose().last().pose(), gui.bufferSource(), Font.DisplayMode.NORMAL, 0, FULL_BRIGHT);
            gui.flushIfUnmanaged();
            return i;
        }
    }
    
    public static int drawString(GuiGraphics gui, Font font, @Nullable String string, float x, float y, int color, boolean drawShadow) {
        if (string == null) return 0;
        else {
            int i = font.drawInBatch(string, x, y, color, drawShadow, gui.pose().last().pose(), gui.bufferSource(), Font.DisplayMode.NORMAL, 0, FULL_BRIGHT);
            gui.flushIfUnmanaged();
            return i;
        }
    }
    
    public static int drawString(GuiGraphics gui, Font font, @Nullable FormattedCharSequence string, float x, float y, int color, boolean drawShadow) {
        if (string == null) return 0;
        else {
            int i = font.drawInBatch(string, x, y, color, drawShadow, gui.pose().last().pose(), gui.bufferSource(), Font.DisplayMode.NORMAL, 0, FULL_BRIGHT);
            gui.flushIfUnmanaged();
            return i;
        }
    }
    
    public static void drawScaledTooltips(GuiGraphics gui, Component text, int x, int y, float size){
        drawScaledTooltips(gui, getFont(), text, x, y, size);
    }
    public static void drawScaledTooltips(GuiGraphics gui, Font font, Component text, int x, int y, float size){
        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.scale(size, size, 1);
        gui.renderTooltip(font, text, (int) (x / size), (int) (y / size));
        pose.popPose();
    }
    
    public static void drawScaledTooltips(GuiGraphics gui, java.util.List<Component> text, int x, int y, float size){
        drawScaledTooltips(gui, getFont(), text, x, y, size);
    }
    public static void drawScaledTooltips(GuiGraphics gui, Font font, java.util.List<Component> text, int x, int y, float size){
        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.scale(size, size, 1);
        gui.renderComponentTooltip(font, text, (int) (x / size), (int) (y / size));
        pose.popPose();
    }
    
    public static void renderScaledTooltip(GuiGraphics gui, ItemStack stack, java.util.List<Component> list, int x, int y, float size) {
        renderScaledTooltip(gui, getFont(), stack, list, x, y, size);
    }
    public static void renderScaledTooltip(GuiGraphics gui, Font font, ItemStack stack, List<Component> list, int x, int y, float size) {
        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.scale(size, size, 1);
        gui.renderComponentTooltip(font, list, (int) (x/size), (int) (y/size), stack);
        pose.popPose();
    }
    
    public static void renderScaledTooltip(GuiGraphics gui, ItemStack stack, int x, int y, float size) {
        renderScaledTooltip(gui, getFont(), stack, x, y, size);
    }
    public static void renderScaledTooltip(GuiGraphics gui, Font font, ItemStack stack, int x, int y, float size) {
        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.scale(size, size, 1);
        gui.renderTooltip(font, stack, (int) (x/size), (int) (y/size));
        pose.popPose();
    }
    
    public static <T extends Component> SplitText splitText(List<T> rawText, int max, float scale) {
        return splitText(toString(rawText), max, scale);
    }
    
    public static SplitText splitText(String text, int max, float scale) {
        SplitText result = new SplitText(scale);
        StringBuilder currentLine = new StringBuilder();
        float currentWidth = 0;
        Font font = getFont();
        String[] words = text.split(" ");
        
        for(String word : words) {
            float wordWidth = (font.width(word + " ") * scale);
            if (!word.equals("\n") && currentWidth + wordWidth <= max) {
                currentWidth += wordWidth;
                currentLine.append(word).append(" ");
            } else {
                result.add(currentLine.toString());
                currentWidth = word.equals("\n") ? 0 : wordWidth;
                currentLine = new StringBuilder();
                if (!word.equals("\n"))
                    currentLine.append(word).append(" ");
            }
        }
        
        return result;
    }
    
    public static SplitText splitText(String text, int max) {
        return splitText(text, max, 1);
    }
    public static <T extends Component> SplitText splitText(List<T> text, int max) {
        return splitText(text, max, 1);
    }
    
    public static <T extends Component> String toString(List<T> components){
        StringBuilder builder = new StringBuilder();
        for(T component : components) {
            builder.append(component.getString());
            if(components.size()>1 && components.size()-1>components.indexOf(component)) builder.append(" \n ");
        }
        return builder.toString();
    }
    public static String listToString(List<String> strings){
        List<Component> list = new ArrayList<>(strings.size());
        strings.forEach(s -> list.add(Text.create(s)));
        return toString(list);
    }
    
    public static <T extends Component> float findScale(List<T> text, int width, int height) {
        return findScale(toString(text), width, height);
    }
    public static float findOptimalScaleForSList(List<String> text, int width, int height) {
        return findScale(listToString(text), width, height);
    }
    public static float findScale(String textString, int width, int height) {
        Font font = getFont();
        float low = 0.01f;
        float high = 1f;
        float bestScale = high;
        
        for (int i = 0; i < 20; i++) {
            float mid = (low + high) / 2;
            
            SplitText lines = splitText(textString, width, mid);
            float textHeight = lines.size() * font.lineHeight * mid;
            
            if (textHeight <= height) {
                bestScale = mid;
                low = mid;
            } else {
                high = mid;
            }
            if (high - low < 0.01f) break;
        }
        
        return bestScale;
    }
    
    public static <T extends Component> float findWidthScale(T text, int width){
        return findWidthScale(text.getString(), width);
    }
    public static float findWidthScale(String text, int width){
        float low = 0.01f;
        float high = 1;
        float bestScale = high;
        Font font = getFont();
        if(font==null) return 1;
        
        for(int i = 0; i < 20; ++i) {
            float mid = (low + high) / 2f;
            float textW = font.width(text) * mid;
            if (textW <= width) {
                bestScale = mid;
                low = mid;
            } else high = mid;
            if (high - low < 0.01f) break;
        }
        
        return bestScale;
    }
}
