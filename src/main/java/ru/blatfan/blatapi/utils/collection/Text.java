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
import java.util.List;

public class Text implements Component {
    protected final MutableComponent component = Component.empty();
    
    protected Text(MutableComponent component) {
        add(component);
    }
    protected Text(List<String> components) {
        components.forEach(component::append);
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
        return create(component.toString());
    }
    public static Text create(int component){
        return create(String.valueOf(component));
    }
    public static Text create(float component){
        return create(String.valueOf(component));
    }
    public static Text create(long component){
        return create(String.valueOf(component));
    }
    public static Text create(double component){
        return create(String.valueOf(component));
    }
    
    public Text copyText(){
        return new Text(component);
    }
    
    @Override
    public MutableComponent copy() {
        return component.copy();
    }
    
    public MutableComponent asComponent(){
        return component.copy();
    }
    
    public Text add(String c){
        component.append(c);
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
        this.component.withStyle(style);
        return this;
    }
    
    public Text withStyle(ChatFormatting style){
        this.component.withStyle(style);
        return this;
    }
    
    public Text withColor(Color color){
        this.component.withStyle(style -> style.withColor(color.getRGB()));
        return this;
    }
    
    public Color getColor(){
        if(component.getStyle().getColor()==null)
            return Color.WHITE;
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