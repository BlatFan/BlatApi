package ru.blatfan.blatapi.client.gui;

import lombok.Getter;

@Getter
public class ScreenArea {
    private final float minX, minY, maxX, maxY;
    
    protected ScreenArea(float minX, float minY, float maxX, float maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }
    
    public ScreenArea multiply(float scale){
        return area(minX*scale, minY*scale, maxX*scale, maxY*scale);
    }
    
    public ScreenArea add(float x, float y){
        return area(minX+x, minY+y, maxX+x, maxY+y);
    }
    
    public boolean inside(double mX, double mY){
        return mX>=minX && mX<=maxX && mY>=minY && mY<=maxY;
    }
    
    public static ScreenArea always(){
        return new ScreenArea(0, 0, 0, 0){
            @Override
            public boolean inside(double mX, double mY) {
                return true;
            }
        };
    }
    public static ScreenArea never(){
        return new ScreenArea(0, 0, 0, 0){
            @Override
            public boolean inside(double mX, double mY) {
                return false;
            }
        };
    }
    public static ScreenArea area(float minX, float minY, float maxX, float maxY){
        return new ScreenArea(minX, minY, maxX, maxY);
    }
    public static ScreenArea area(float width, float height){
        return new ScreenArea(0, 0, width, height);
    }
    public static ScreenArea box(float minX, float minY, float width, float height){
        return new ScreenArea(minX, minY, minX+width, minY+height);
    }
}