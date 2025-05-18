package ru.blatfan.blatapi.creative_tab_filter;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class FilterButton extends Button {
    protected ResourceLocation iconResource;
    protected int iconU;
    protected int iconV;
    
    public FilterButton(int x, int y, Component tooltip, OnPress onPress, ResourceLocation iconResource, int iconU, int iconV) {
        super(x, y, 20, 20, tooltip, onPress, DEFAULT_NARRATION);
        this.iconResource = iconResource;
        this.iconU = iconU;
        this.iconV = iconV;
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_93658_, int p_93659_, float p_93660_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1f, 1f, 1f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        guiGraphics.blitNineSliced(WIDGETS_LOCATION, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 20, 4, 200, 20, 0, this.getTextureY());
        guiGraphics.blit(this.iconResource, this.getX() + 2, this.getY() + 2, this.iconU, this.iconV, 16, 16);
    }
    
    @Override
    public boolean isFocused() {
        return false;
    }
    
    public int getTextureY() {
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (this.isHoveredOrFocused()) {
            i = 2;
        }
        
        return 46 + i * 20;
    }
}