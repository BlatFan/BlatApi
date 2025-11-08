package ru.blatfan.blatapi.utils.collection;

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
import java.util.ArrayList;
import java.util.List;

public class Text implements Component {
    protected final List<String> components = new ArrayList<>();
    protected final List<ChatFormatting> chatFormattings = new ArrayList<>();
    protected Style style = Style.EMPTY;
    
    protected Text(MutableComponent component) {
        add(component);
    }
    protected Text(List<String> components) {
        this.components.addAll(components);
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
    
    public Text copyText(){
        return new Text(components);
    }
    
    @Override
    public MutableComponent copy() {
        return asComponent().copy();
    }
    
    public MutableComponent asComponent(){
        MutableComponent component = Component.empty();
        components.forEach(c -> component.append(Component.translatable(c)));
        if(style!=null) {
            component.withStyle(style);
            chatFormattings.forEach(component::withStyle);
        }
        return component.copy();
    }
    
    public Text add(String c){
        components.add(c);
        return this;
    }
    
    public Text add(Component c){
        components.add(c.getString());
        return this;
    }
    
    public Text space(){
        return add(" ");
    }
    
    public Text withStyle(Style style){
        this.style=style;
        return this;
    }
    
    public Text withStyle(ChatFormatting style){
        chatFormattings.add(style);
        return this;
    }
    
    public Text withColor(Color color){
        this.style.withColor(color.getRGB());
        return this;
    }
    
    public Color getColor(){
        if(asComponent().getStyle().getColor()==null)
            return Color.WHITE;
        return new Color(asComponent().getStyle().getColor().getValue());
    }
    
    @OnlyIn(Dist.CLIENT)
    public static String getCurrentLanguageCode() {
        return Minecraft.getInstance().getLanguageManager().getSelected();
    }
    
    @Override
    public Style getStyle() {
        return style;
    }
    
    @Override
    public ComponentContents getContents() {
        return asComponent().getContents();
    }
    
    @Override
    public List<Component> getSiblings() {
        return asComponent().getSiblings();
    }
    
    @Override
    public FormattedCharSequence getVisualOrderText() {
        return asComponent().getVisualOrderText();
    }
    
    @Override
    public String getString() {
        return asComponent().getString();
    }
}