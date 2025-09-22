package ru.blatfan.blatapi.utils;

import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.List;

public class Text implements Component {
    private final MutableComponent component;
    @Getter
    private Color lastColor = Color.WHITE;
    
    private Text(MutableComponent component) {
        this.component = component;
    }
    
    public static Text create(Component component){
        return new Text(component.copy());
    }
    public static Text create(String component, Object... args){
        return new Text(Component.translatable(component, args));
    }
    
    public static Text create(String component){
        return new Text(Component.translatable(component));
    }
    
    public static Text create(ResourceLocation component){
        return new Text(Component.translatable(component.toString()));
    }
    
    public MutableComponent asComponent(){
        return component.copy();
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
    
    public Text withStyle(Style style){
        component.withStyle(style);
        return this;
    }
    
    public Text withStyle(ChatFormatting style){
        component.withStyle(style);
        return this;
    }
    
    public Text withColor(Color color){
        component.withStyle(style -> style.withColor(color.getRGB()));
        lastColor=color;
        return this;
    }
    
    public Color getColor(){
        return new Color(component.getStyle().getColor().getValue());
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