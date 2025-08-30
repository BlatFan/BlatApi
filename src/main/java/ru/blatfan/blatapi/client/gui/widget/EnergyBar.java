package ru.blatfan.blatapi.client.gui.widget;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.energy.IEnergyStorage;
import ru.blatfan.blatapi.BlatApi;

public class EnergyBar extends AbstractWidget {
  public final ResourceLocation ENERGY_BAR;
  private final IEnergyStorage energy;
  
  public EnergyBar(int pX, int pY, int pWidth, ResourceLocation energyBar, IEnergyStorage energy) {
    super(pX, pY, pWidth, 62*(pWidth/16), Component.literal("Energy Bar"));
      ENERGY_BAR = energyBar;
      this.energy = energy;
  }
  public EnergyBar(int pX, int pY, int pWidth, IEnergyStorage energy) {
    this(pX, pY, pWidth, BlatApi.loc("textures/gui/energy_bar.png"), energy);
  }
  
  public boolean isMouseover(int mouseX, int mouseY) {
    return getX() < mouseX && mouseX < getX() + width
        && getY() < mouseY && mouseY < getY() + getHeight();
  }
  
  @Override
  protected void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float pPartialTick) {
    if (!visible)
      return;
    gui.blit(ENERGY_BAR, getX(), getY(), width, 0, width, getHeight(), width*2, getHeight());
    final float pct = Math.min((float) energy.getEnergyStored() / energy.getMaxEnergyStored(), 1.0F);
    gui.blit(ENERGY_BAR, getX(), getY(), 0, 0, width, getHeight() - (int) (getHeight() * pct), width*2, getHeight());
    if (visible && this.isMouseover(mouseX, mouseY)) {
      String tt = energy.getEnergyStored() + "FE /" + energy.getMaxEnergyStored()+"FE";
      List<Component> list = new ArrayList<>();
      list.add(Component.translatable(tt));
      gui.renderComponentTooltip(Minecraft.getInstance().font, list, mouseX, mouseY);
    }
  }
  
  @Override
  protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {}
}
