package ru.blatfan.blatapi.utils.collection;

import com.google.gson.JsonArray;
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
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
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
    public static Text createWithFont(Component component, ResourceLocation font){
        return new Text(component.copy().withStyle(style -> style.withFont(font)));
    }
    public static Text create(String component, Object... args) {
        return new Text(Component.translatable(component, args));
    }
    public static Text createWithFont(String component, ResourceLocation font, Object... args) {
        return new Text(Component.translatable(component, args).withStyle(style -> style.withFont(font)));
    }
    public static Text create(String component){
        return new Text(Component.translatable(component));
    }
    public static Text createWithFont(String component, ResourceLocation font){
        return new Text(Component.translatable(component).withStyle(style -> style.withFont(font)));
    }
    public static Text create(){
        return new Text(Component.empty());
    }
    public static List<Text> createFromComponentList(List<Component> componentList){
        List<Text> list = new ArrayList<>();
        componentList.forEach(c -> list.add(Text.create(c)));
        return list;
    }
    public static List<Text> createFromStringList(List<String> stringList){
        List<Text> list = new ArrayList<>();
        stringList.forEach(c -> list.add(Text.create(c)));
        return list;
    }
    public static List<Text> createFromJsonList(JsonArray array){
        List<Text> list = new ArrayList<>();
        array.forEach(c -> list.add(Text.create(c.getAsString())));
        return list;
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
    public static Text create(Object component){
        return create(String.valueOf(component));
    }
    
    public static List<Component> toComponentList(List<Text> textList){
        List<Component> list = new ArrayList<>();
        textList.forEach(c -> list.add(c.asComponent()));
        return list;
    }
    
    public Text copyText(){
        return new Text(copy());
    }
    @Override
    public @NotNull MutableComponent copy() {
        return component.copy();
    }
    public MutableComponent asComponent(){
        return copy();
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
        return add(create(c));
    }
    public Text add(Object c){
        component.append(c.toString());
        return this;
    }
    public Text add(Object c, ResourceLocation font){
        component.append(Text.create(c.toString()).withStyle(style -> style.withFont(font)));
        return this;
    }
    public Text add(Component c){
        component.append(c.copy());
        return this;
    }
    public Text add(Component c, ResourceLocation font){
        component.append(c.copy().withStyle(style -> style.withFont(font)));
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
        return withStyle(style -> style.withColor(color.getRGB()));
    }
    
    public Color getColor(){
        if(getStyle().getColor()==null) return Color.WHITE;
        return new Color(getStyle().getColor().getValue());
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
    
    public List<Text> getTextSiblings() {
        List<Text> list = new ArrayList<>();
        for (Component sibling : getSiblings())
            list.add(create(sibling));
        return list;
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
        return component.toString();
    }
    
    public boolean isEmpty() {
        return getString().isEmpty();
    }
}