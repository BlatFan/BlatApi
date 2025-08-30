package ru.blatfan.blatapi.client.gui.widget;

import java.util.ArrayList;
import java.util.List;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.render.FluidRenderMap;

public class FluidBar extends AbstractWidget {
  public final ResourceLocation FLUID_WIDGET;
  private final IFluidHandler fluidHandler;
  private final int tank;
  
  public FluidBar(int pX, int pY, int pWidth, ResourceLocation fluidWidget, IFluidHandler fluidHandler, int tank) {
    super(pX, pY, pWidth, 62*(pWidth/16), Component.literal("Fluid Bar"));
    FLUID_WIDGET = fluidWidget;
      this.fluidHandler = fluidHandler;
      this.tank = tank;
  }
  public FluidBar(int pX, int pY, int pWidth, IFluidHandler fluidHandler, int tank){
    this(pX, pY, pWidth, BlatApi.loc("textures/gui/fluid.png"), fluidHandler, tank);
  }
  
  public boolean isMouseover(int mouseX, int mouseY) {
    return getX() < mouseX && mouseX < getX() + width
        && getY() < mouseY && mouseY < getY() + getHeight();
  }
  
  @Override
  protected void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float pPartialTick) {
    gui.blit(FLUID_WIDGET,
        getX(), getY(), 0, 0,
        width, height,
        width, height);
    //NOW the fluid part
    if (!(fluidHandler == null || fluidHandler.getTankCapacity(tank) == 0 || fluidHandler.getFluidInTank(tank).getAmount() == 0)) {
      float capacity = fluidHandler.getTankCapacity(tank);
      float amount = fluidHandler.getFluidInTank(tank).getAmount();
      float scale = amount / capacity;
      int fluidAmount = (int) (scale * height);
      TextureAtlasSprite sprite = FluidRenderMap.getCachedFluidTexture(fluidHandler.getFluidInTank(tank), FluidRenderMap.FluidFlow.STILL);
      if (fluidHandler.getFluidInTank(tank).getFluid() == Fluids.WATER)
        RenderSystem.setShaderColor(0, 0, 1, 1);
      int xPosition = getX() + 1;
      int yPosition = getY() + 1;
      int maximum = height - 2;
      int desiredWidth = width - 2;
      int desiredHeight = fluidAmount - 2;
      gui.blit(xPosition, yPosition + (maximum - desiredHeight), 0, desiredWidth, desiredHeight, sprite);
      RenderSystem.setShaderColor(1, 1, 1, 1);
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
  
  @Override
  protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {}
}
