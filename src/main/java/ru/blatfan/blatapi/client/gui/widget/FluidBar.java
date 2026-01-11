package ru.blatfan.blatapi.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.render.FluidRenderMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FluidBar extends AbstractWidget {
  public final ResourceLocation FLUID_WIDGET;
  private final IFluidHandler fluidHandler;
  private final int tank;
  private final float scale;
  private final Color barColor;
  
  public FluidBar(int pX, int pY, int pWidth, ResourceLocation fluidWidget, IFluidHandler fluidHandler, int tank, Color barColor) {
    super(pX, pY, pWidth, 62*(pWidth/16), Component.literal("Fluid Bar"));
    this.FLUID_WIDGET = fluidWidget;
    this.fluidHandler = fluidHandler;
    this.tank = tank;
    this.scale = pWidth/16f;
    this.barColor = barColor;
  }
  public FluidBar(int pX, int pY, int pWidth, IFluidHandler fluidHandler, int tank, Color barColor){
    this(pX, pY, pWidth, BlatApi.loc("textures/gui/fluid.png"), fluidHandler, tank, barColor);
  }
  public FluidBar(int pX, int pY, int pWidth, IFluidHandler fluidHandler, int tank){
    this(pX, pY, pWidth, BlatApi.loc("textures/gui/fluid.png"), fluidHandler, tank, Color.WHITE);
  }
  
  public boolean isMouseover(int mouseX, int mouseY) {
    return getX() < mouseX && mouseX < getX() + width
        && getY() < mouseY && mouseY < getY() + getHeight();
  }
  
  @Override
  protected void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float pPartialTick) {
    gui.setColor(barColor.getRed()/255f, barColor.getGreen()/255f, barColor.getBlue()/255f, 1);
    gui.blit(FLUID_WIDGET,
        getX(), getY(), 0, 0,
        width, height,
        width*2, height);
    gui.setColor(1, 1, 1, 1);
    
    if (!(fluidHandler == null || fluidHandler.getTankCapacity(tank) == 0 || fluidHandler.getFluidInTank(tank).getAmount() == 0)) {
      float capacity = fluidHandler.getTankCapacity(tank);
      float amount = fluidHandler.getFluidInTank(tank).getAmount();
      FluidStack fluidStack = fluidHandler.getFluidInTank(tank);

      float fillRatio = Math.min(1.0f, amount / capacity);
      int fluidHeight = Math.round(height * fillRatio);
      
      TextureAtlasSprite sprite = FluidRenderMap.getCachedFluidTexture(fluidStack, FluidRenderMap.FluidFlow.STILL);
      Color fluidColor = FluidRenderMap.getTintColor(fluidStack);

      int xPosition = Math.round(getX() + scale);
      int yPosition = Math.round(getY() + scale);
      int maxHeight = Math.round(height - 2 * scale);
      int renderWidth = Math.round(width - 2 * scale);
      int renderHeight = Math.round(fluidHeight - 2 * scale);
      
      if (renderHeight > 0) {
        int tileSize = (int) (16*scale);
        int yOffset = 0;
        int remainingHeight = renderHeight;
        
        while (remainingHeight > 0) {
          int currentTileHeight = Math.min(tileSize, remainingHeight);
          int renderY = yPosition + maxHeight - yOffset - currentTileHeight;
          
          gui.blit(xPosition, renderY, 0, renderWidth, currentTileHeight, sprite,
              fluidColor.getRed()/255f, fluidColor.getGreen()/255f, fluidColor.getBlue()/255f, fluidColor.getAlpha()/255f);
          
          yOffset += currentTileHeight;
          remainingHeight -= currentTileHeight;
        }
      }
      if (this.isMouseover(mouseX, mouseY)) {
        String tt = "0mb";
        FluidStack current = fluidHandler.getFluidInTank(tank);
        if (!current.isEmpty()) {
          tt = current.getAmount() + "mb /" + fluidHandler.getTankCapacity(tank) + "mb " + current.getDisplayName().getString();
        }
        List<Component> list = new ArrayList<>();
        list.add(Component.translatable(tt));
        gui.renderComponentTooltip(Minecraft.getInstance().font, list, mouseX, mouseY);
      }
    }
    
    gui.setColor(barColor.getRed()/255f, barColor.getGreen()/255f, barColor.getBlue()/255f, 1);
    gui.blit(FLUID_WIDGET,
        getX(), getY(), width, 0,
        width, height,
        width*2, height);
    gui.setColor(1, 1, 1, 1);
  }
  
  @Override
  protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {}
}
