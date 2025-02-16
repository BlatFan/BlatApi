package ru.blatfan.blatapi.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class GuiUtil {
    public static void drawScaledString(GuiGraphics guiGraphics, String text, int x, int y, Color color, float size){
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(size, size, size);
        guiGraphics.drawString(Minecraft.getInstance().font, text, x / size, y / size, ColorHelper.getColor(color), true);
        guiGraphics.pose().popPose();
    }
    public static void drawScaledString(GuiGraphics guiGraphics, Component text, int x, int y, Color color, float size){
        drawScaledString(guiGraphics, text.getString(), x, y, color, size);
    }
    public static void drawScaledCentreString(GuiGraphics guiGraphics, String text, int x, int y, Color color, float size){
        drawScaledString(guiGraphics, text, x-Minecraft.getInstance().font.width(text)/2, y, color, size);
    }
    public static void drawScaledCentreString(GuiGraphics guiGraphics, Component text, int x, int y, Color color, float size){
        drawScaledCentreString(guiGraphics, text.getString(), x, y, color, size);
    }
}