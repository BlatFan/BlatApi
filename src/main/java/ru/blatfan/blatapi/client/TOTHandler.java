package ru.blatfan.blatapi.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.render.IItemOverrideRenderer;
import ru.blatfan.blatapi.client.render.ItemOverrideTexture;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TOTHandler {
    public static List<IItemOverrideRenderer> renderers = Collections.synchronizedList(new LinkedList<>());
    
    static {
        if(BlatApi.DEV)
            renderers.add(new ItemOverrideTexture(ResourceLocation.parse("textures/item/diamond.png"), List.of(Items.EMERALD), Color.GREEN));
    }
    
    public static void render(GuiGraphics gui, ItemStack stack, int x, int y){
        List<IItemOverrideRenderer> list = new ArrayList<>();
        for(IItemOverrideRenderer iot : renderers) if(iot.getItems().contains(stack.getItem())) list.add(iot);
        if(list.isEmpty()) return;
        RenderSystem.disableDepthTest();
        for(IItemOverrideRenderer iot : list){
            int i = 1+list.indexOf(iot);
            
            gui.pose().pushPose();
            gui.pose().translate(0,0,-150+i);
            
            iot.render(gui, stack, x, y);
            
            RenderSystem.setShaderColor(1,1,1,1);
            gui.pose().popPose();
        }
    }
}
