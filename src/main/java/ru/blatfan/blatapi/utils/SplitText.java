package ru.blatfan.blatapi.utils;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class SplitText extends ArrayList<String> {
    private final float scale;
    
    public SplitText(float scale) {
        this.scale = scale;
    }
    
    public int height(){
        return (int) (size()*scale* Minecraft.getInstance().font.lineHeight);
    }
    public float scale(){
        return scale;
    }
}