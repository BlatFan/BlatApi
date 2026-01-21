package ru.blatfan.blatapi.utils.collection;

import ru.blatfan.blatapi.utils.GuiUtil;

import java.util.ArrayList;

public class SplitText extends ArrayList<Text> {
    private final float scale;
    
    public SplitText(float scale) {
        this.scale = scale;
    }
    
    public boolean add(String text) {
        return super.add(Text.create(text));
    }
    
    public float scale(){
        return scale;
    }
    
    public int height() {
        return (int) (size()*scale()*GuiUtil.getFont().lineHeight);
    }
}