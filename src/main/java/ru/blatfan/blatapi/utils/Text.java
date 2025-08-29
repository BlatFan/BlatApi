package ru.blatfan.blatapi.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class Text implements Component {
    private final MutableComponent component;
    
    private Text(MutableComponent component) {
        this.component = component;
    }
    
    public static Text create(Component component){
        return new Text(component.copy());
    }
    
    public static Text create(String component){
        return new Text(Component.translatable(component));
    }
    
    public Text add(String c){
        component.append(Component.translatable(c));
        return this;
    }
    
    public Text add(Component c){
        component.append(c);
        return this;
    }
    
    public Text space(){
        return add(" ");
    }
    
    @OnlyIn(Dist.CLIENT)
    public static String getCurrentLanguageCode() {
        return Minecraft.getInstance().getLanguageManager().getSelected();
    }
    
    @Override
    public Style getStyle() {
        return component.getStyle();
    }
    
    @Override
    public ComponentContents getContents() {
        return component.getContents();
    }
    
    @Override
    public List<Component> getSiblings() {
        return component.getSiblings();
    }
    
    @Override
    public FormattedCharSequence getVisualOrderText() {
        return component.getVisualOrderText();
    }
    
    @Override
    public String getString() {
        return component.getString();
    }
}