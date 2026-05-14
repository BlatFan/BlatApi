package ru.blatfan.blatapi.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.energy.IEnergyStorage;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.utils.collection.Text;

import java.util.ArrayList;
import java.util.List;

public class EnergyBar extends AbstractWidget {
  protected final ResourceLocation texture;
  protected final IEnergyStorage energy;
  
  public EnergyBar(int pX, int pY, int pWidth, ResourceLocation energyBar, IEnergyStorage energy) {
    super(pX, pY, pWidth, 62*(pWidth/16), Component.literal("Energy Bar"));
      texture = energyBar;
      this.energy = energy;
  }
  public EnergyBar(int pX, int pY, int pWidth, IEnergyStorage energy) {
    this(pX, pY, pWidth, BlatApi.guiLoc("energy_bar"), energy);
  }
  
  public boolean isMouseover(int mouseX, int mouseY) {
    return getX() < mouseX && mouseX < getX() + width
        && getY() < mouseY && mouseY < getY() + getHeight();
  }
  
  @Override
  protected void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float pPartialTick) {
    if (!visible)
      return;
    gui.blit(texture, getX(), getY(), width, 0, width, getHeight(), width*2, getHeight());
    final float pct = Math.min((float) energy.getEnergyStored() / energy.getMaxEnergyStored(), 1);
    gui.blit(texture, getX(), getY(), 0, 0, width, getHeight() - (int) (getHeight() * pct), width*2, getHeight());
    if (this.isMouseover(mouseX, mouseY))
      renderTooltip(gui, mouseX, mouseY, pPartialTick);
  }
  
  protected void renderTooltip(GuiGraphics gui, int mouseX, int mouseY, float pPartialTick){
    String tt = energy.getEnergyStored() + "FE /" + energy.getMaxEnergyStored()+"FE";
    List<Component> list = new ArrayList<>();
    list.add(Text.create(tt));
    gui.renderComponentTooltip(Minecraft.getInstance().font, list, mouseX, mouseY);
  }
  
  @Override
  protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {}
}
