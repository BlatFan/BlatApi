package ru.blatfan.blatapi.utils;

import org.joml.Vector3f;
import java.awt.*;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class ColorHelper {

    public static Color getColor(int decimal) {
        return new Color(decimal);
    }

    public static void RGBToHSV(Color color, float[] hsv) {
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsv);
    }
    
    public static Color getColor(String color) {
        if(!color.startsWith("#")) return Color.decode("#"+color);
        return Color.decode(color);
    }
    
    public static int getColor(Color color) {
        return color.getRGB();
    }

    public static int getColor(int r, int g, int b) {
        return getColor(new Color(r,g,b));
    }

    public static int getColor(int r, int g, int b, int a) {
        return getColor(new Color(r,g,b,a));
    }

    public static int getColor(float r, float g, float b, float a) {
        return getColor(new Color(r,g,b,a));
    }
    
    public static Color rainbowColor(float ticks) {
        int r = (int) (Math.sin(ticks) * 127 + 128);
        int g = (int) (Math.sin(ticks + Math.PI / 2) * 127 + 128);
        int b = (int) (Math.sin(ticks + Math.PI) * 127 + 128);
        return new Color(r, g, b);
    }
    
    public static Color getBlendColor(ArrayList<Color> colors) {
        float f = 0.0F;
        float f1 = 0.0F;
        float f2 = 0.0F;
        int j = 0;
        
        for(Color color : colors) {
            int k = ColorHelper.getColor(color);
            f += (float) ((k >> 16 & 255)) / 255.0F;
            f1 += (float) ((k >> 8 & 255)) / 255.0F;
            f2 += (float) ((k >> 0 & 255)) / 255.0F;
            j += 1;
        }
        
        f = f / (float)j * 255.0F;
        f1 = f1 / (float)j * 255.0F;
        f2 = f2 / (float)j * 255.0F;
        return ColorHelper.getColor((int) f << 16 | (int) f1 << 8 | (int) f2);
    }
    
    public static Vector3f getColorV3f(Color color){
        return new Vector3f(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static Color darker(Color color, int times) {
        return darker(color, times, 0.7f);
    }

    public static Color darker(Color color, int power, float factor) {
        float FACTOR = (float) Math.pow(factor, power);
        return new Color(Math.max((int) (color.getRed() * FACTOR), 0),
                Math.max((int) (color.getGreen() * FACTOR), 0),
                Math.max((int) (color.getBlue() * FACTOR), 0),
                color.getAlpha());
    }

    public static Color brighter(Color color, int power) {
        return brighter(color, power, 0.7f);
    }

    public static Color brighter(Color color, int power, float factor) {
        float FACTOR = (float) Math.pow(factor, power);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int alpha = color.getAlpha();

        int i = (int) (1.0 / (1.0 - FACTOR));
        if (r == 0 && g == 0 && b == 0) {
            return new Color(i, i, i, alpha);
        }
        if (r > 0 && r < i) r = i;
        if (g > 0 && g < i) g = i;
        if (b > 0 && b < i) b = i;

        return new Color(Math.min((int) (r / FACTOR), 255),
                Math.min((int) (g / FACTOR), 255),
                Math.min((int) (b / FACTOR), 255),
                alpha);
    }
}