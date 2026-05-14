package ru.blatfan.blatapi.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.joml.Matrix4f;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.render.FluidRenderMap;
import ru.blatfan.blatapi.utils.GuiUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FluidBar extends AbstractWidget {
  protected final ResourceLocation texture;
  protected final IFluidHandler fluidHandler;
  protected final int tank;
  protected final float scale;
  protected final Color barColor;
  
  public FluidBar(int pX, int pY, int pWidth, ResourceLocation fluidWidget, IFluidHandler fluidHandler, int tank, Color barColor) {
    super(pX, pY, pWidth, (int) (62*(pWidth/16f)), Component.literal("Fluid Bar"));
    this.texture = fluidWidget;
    this.fluidHandler = fluidHandler;
    this.tank = tank;
    this.scale = pWidth/16f;
    this.barColor = barColor;
  }
  public FluidBar(int pX, int pY, int pWidth, ResourceLocation fluidWidget, FluidTank fluidHandler, Color barColor) {
    this(pX, pY, pWidth, fluidWidget, fluidHandler, 0, barColor);
  }
  public FluidBar(int pX, int pY, int pWidth, IFluidHandler fluidHandler, int tank, Color barColor){
    this(pX, pY, pWidth, BlatApi.guiLoc("fluid"), fluidHandler, tank, barColor);
  }
  public FluidBar(int pX, int pY, int pWidth, FluidTank fluidHandler, Color barColor){
    this(pX, pY, pWidth, BlatApi.guiLoc("fluid"), fluidHandler, barColor);
  }
  public FluidBar(int pX, int pY, int pWidth, IFluidHandler fluidHandler, int tank){
    this(pX, pY, pWidth, fluidHandler, tank, Color.WHITE);
  }
  public FluidBar(int pX, int pY, int pWidth, FluidTank fluidHandler){
    this(pX, pY, pWidth, fluidHandler, Color.WHITE);
  }
  
  public boolean isMouseover(int mouseX, int mouseY) {
    return getX() < mouseX && mouseX < getX() + width
        && getY() < mouseY && mouseY < getY() + getHeight();
  }
  
  @Override
  protected void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float pPartialTick) {
    gui.setColor(barColor.getRed()/255f, barColor.getGreen()/255f, barColor.getBlue()/255f, 1);
    gui.blit(texture,
        getX(), getY(), 0, 0,
        width, height,
        width*2, height);
    gui.setColor(1, 1, 1, 1);
    
    if (!(fluidHandler == null || fluidHandler.getTankCapacity(tank) == 0 || fluidHandler.getFluidInTank(tank).getAmount() == 0))
      renderFluid(gui, pPartialTick);
    
    gui.setColor(barColor.getRed()/255f, barColor.getGreen()/255f, barColor.getBlue()/255f, 1);
    gui.blit(texture,
        getX(), getY(), width, 0,
        width, height,
        width*2, height);
    gui.setColor(1, 1, 1, 1);
    
    if (!(fluidHandler == null || fluidHandler.getTankCapacity(tank) == 0 || fluidHandler.getFluidInTank(tank).getAmount() == 0))
      if (this.isMouseover(mouseX, mouseY))
        renderTooltips(gui, mouseX, mouseY, pPartialTick);
  }
  
  protected void renderFluid(GuiGraphics gui, float pPartialTick){
    float capacity = fluidHandler.getTankCapacity(tank);
    float amount = fluidHandler.getFluidInTank(tank).getAmount();
    FluidStack fluidStack = fluidHandler.getFluidInTank(tank);
    
    float fillRatio = Math.min(1, amount / capacity);
    int fluidHeight = Math.round(height * fillRatio);
    
    TextureAtlasSprite sprite = FluidRenderMap.getCachedFluidTexture(fluidStack, FluidRenderMap.FluidFlow.STILL);
    Color color = FluidRenderMap.getTintColor(fluidStack);
    
    int maxHeight = Math.round(height - 2 * scale);
    
    int renderWidth = Math.round(width - 2 * scale);
    int renderHeight = Math.max(0, Math.round(fluidHeight - 2 * scale));
    
    int xPosition = Math.round(getX() + scale);
    int yPosition = Math.round(getY() + scale + (maxHeight - renderHeight));
    
    RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
    Matrix4f matrix = gui.pose().last().pose();
    RenderSystem.setShaderColor(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/255f);
    RenderSystem.enableBlend();
    int xTileCount = renderWidth / 16;
    float xRemainder = renderWidth - (float)(xTileCount * 16);
    int yTileCount = renderHeight / 16;
    float yRemainder = renderHeight - (float)(yTileCount * 16);
    float yStart = yPosition + renderHeight;
    
    for(int xTile = 0; xTile <= xTileCount; ++xTile) {
      for(int yTile = 0; yTile <= yTileCount; ++yTile) {
        float width = xTile == xTileCount ? xRemainder : 16;
        float height = yTile == yTileCount ? yRemainder : 16;
        float x = xPosition + (float)(xTile * 16);
        float y = yStart - (float)((yTile + 1) * 16);
        if (width > 0 && height > 0) {
          float maskTop = 16 - height;
          float maskRight = 16 - width;
          GuiUtil.drawTextureWithMasking(matrix, x, y, sprite, maskTop, maskRight);
        }
      }
    }
    
    RenderSystem.setShaderColor(1, 1, 1, 1);
    RenderSystem.disableBlend();
  }
  
  protected void renderTooltips(GuiGraphics gui, int mouseX, int mouseY, float pPartialTick){
    FluidStack current = fluidHandler.getFluidInTank(tank);
    String amountText = current.getAmount() + " / " + fluidHandler.getTankCapacity(tank) + " mB";
    List<Component> list = new ArrayList<>();
    list.add(current.getDisplayName());
    list.add(Component.literal(amountText));
    gui.renderComponentTooltip(Minecraft.getInstance().font, list, mouseX, mouseY);
  }
  
  @Override
  protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {}
}