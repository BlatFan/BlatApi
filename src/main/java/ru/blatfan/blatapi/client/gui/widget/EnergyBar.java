package ru.blatfan.blatapi.client.gui.widget;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import ru.blatfan.blatapi.BlatApi;

public class EnergyBar {
  public ResourceLocation ENERGY_BAR = BlatApi.loc("textures/gui/energy_bar.png");
  @Getter @Setter
  private int x = 154;
  @Getter @Setter
  private int y = 8;
  private final int capacity;
  @Getter @Setter
  private int width = 16;
  @Getter @Setter
  private int height = 62;
  @Getter @Setter
  private int guiLeft;
  @Getter @Setter
  private int guiTop;
  @Getter @Setter
  private boolean visible = true;
  private final Font font;

  public EnergyBar(Font font, int cap) {
    this.capacity = cap;
    this.font = font;
  }

  public boolean isMouseover(int mouseX, int mouseY) {
    return guiLeft + x < mouseX && mouseX < guiLeft + x + width
        && guiTop + y < mouseY && mouseY < guiTop + y + getHeight();
  }

  public void draw(GuiGraphics gg, float energ) {
    if (!visible)
      return;
    int relX = guiLeft + x;
    int relY = guiTop + y;
    gg.blit(ENERGY_BAR, relX, relY, 16, 0, width, getHeight(), 32, getHeight());
    final float pct = Math.min(energ / capacity, 1.0F);
    gg.blit(ENERGY_BAR, relX, relY, 0, 0, width, getHeight() - (int) (getHeight() * pct), 32, getHeight());
  }

  public void renderHoveredToolTip(GuiGraphics ms, int mouseX, int mouseY, int energ) {
    if (visible && this.isMouseover(mouseX, mouseY)) {
      String tt = energ + "/" + this.capacity;
      List<Component> list = new ArrayList<>();
      list.add(Component.translatable(tt));
      ms.renderComponentTooltip(font, list, mouseX, mouseY);
    }
  }
}
