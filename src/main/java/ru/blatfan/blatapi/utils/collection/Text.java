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
import java.util.function.UnaryOperator;

public class Text implements Component {
    protected final MutableComponent component;
    
    protected Text(MutableComponent component) {
        this.component=component;
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
    public static Text create(){
        return new Text(Component.empty());
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
    
    public Text add(int c){
        return add(String.valueOf(c));
    }
    public Text add(float c){
        return add(String.valueOf(c));
    }
    public Text add(long c){
        return add(String.valueOf(c));
    }
    public Text add(double c){
        return add(String.valueOf(c));
    }
    public Text add(ResourceLocation c){
        return add(c.toString());
    }
    public Text add(String c){
        return add(Component.translatable(c));
    }
    public Text add(Component c){
        component.append(c);
        return this;
    }
    
    public Text space(){
        return add(" ");
    }
    
    public Text setStyle(Style style) {
        this.component.setStyle(style);
        return this;
    }
    public Text withStyle(Style style){
        this.component.withStyle(style);
        return this;
    }
    public Text withStyle(ChatFormatting style){
        this.component.withStyle(style);
        return this;
    }
    public Text withStyle(UnaryOperator<Style> style){
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
    
    @Override
    public String toString() {
        return getString();
    }
}