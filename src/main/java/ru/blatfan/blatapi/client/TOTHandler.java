package ru.blatfan.blatapi.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemStack;
import ru.blatfan.blatapi.client.render.IItemOverrideRenderer;

import java.util.ArrayList;
import java.util.List;

public class TOTHandler {
    public static List<IItemOverrideRenderer> textures = new ArrayList<>();
    
    public static void render(PoseStack gui, ItemStack stack, int x, int y){
        List<IItemOverrideRenderer> list = new ArrayList<>();
        for(IItemOverrideRenderer iot : textures) if(iot.getItems().contains(stack.getItem())) list.add(iot);
        if(list.isEmpty()) return;
        RenderSystem.disableDepthTest();
        for(IItemOverrideRenderer iot : list){
            int i = 1+list.indexOf(iot);
            
            gui.pushPose();
            gui.translate(0,0,-150+i);
            
            iot.render(gui, stack, x, y);
            
            RenderSystem.setShaderColor(1,1,1,1);
            gui.popPose();
        }
    }
}
