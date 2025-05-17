package ru.blatfan.blatapi.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;
import ru.blatfan.blatapi.client.render.ItemOverrideTexture;
import ru.blatfan.blatapi.utils.GuiUtil;

import java.util.ArrayList;
import java.util.List;

public class TOTHandler {
    public static List<ItemOverrideTexture> textures = new ArrayList<>();
    
    public static void render(PoseStack gui, ItemStack stack, int x, int y){
        List<ItemOverrideTexture> list = new ArrayList<>();
        for(ItemOverrideTexture iot : textures) if(iot.items().contains(stack.getItem())) list.add(iot);
        if(list.isEmpty()) return;
        RenderSystem.disableDepthTest();
        for(ItemOverrideTexture iot : list){
            int i = 1+list.indexOf(iot);
            TextColor color = TextColor.fromRgb(iot.color().getRGB());
            
            gui.pushPose();
            gui.translate(0,0,-150+i);
            
            RenderSystem.setShaderTexture(0, iot.texture());
            RenderSystem.setShaderColor((color.getValue() >> 16 & 255) / 255.0f, (color.getValue() >> 8 & 255) / 255.0f, (color.getValue() & 255) / 255.0f, 1.0f);
            
            GuiUtil.blit(gui, x, y, 16, 16, 0, 0, 16, 16, 16, 16);
            
            RenderSystem.setShaderColor(1,1,1,1);
            gui.popPose();
        }
    }
}
