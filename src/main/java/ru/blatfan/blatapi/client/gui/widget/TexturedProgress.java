package ru.blatfan.blatapi.client.gui.widget;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import ru.blatfan.blatapi.BlatApi;

public class TexturedProgress{
  protected final int x;
  protected final int y;
  protected final int width;
  protected final int height;
  protected final ResourceLocation texture;
  public int guiLeft;
  public int guiTop;
  public int max = 1;
  @Setter
  protected boolean topDown = false;
  
  public TexturedProgress(int x, int y) {
    this(x, y, BlatApi.loc("textures/gui/progress_bar.png"));
  }
  public TexturedProgress(int x, int y, ResourceLocation texture) {
    this(x, y, 22, 16, texture);
  }

  public TexturedProgress(int x, int y, int width, int height, ResourceLocation texture) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.texture = texture;
  }

  public boolean isMouseover(int mouseX, int mouseY) {
    return guiLeft + x <= mouseX && mouseX <= guiLeft + x + width
        && guiTop + y <= mouseY && mouseY <= guiTop + y + height;
  }

  public void draw(GuiGraphics gg, float current) {
    int relX;
    int relY;
    relX = guiLeft + x;
    relY = guiTop + y;
    if (this.topDown) {
      gg.blit(texture, relX, relY, 0, 0, width, height, width, height * 2);
      int rHeight = height - (int) (height * Math.min(current / max, 1.0F));
      if (current != 0)
        gg.blit(texture, relX, relY, 0, height, width, height-rHeight, width, height * 2);
    }
    else { //Left-Right mode
      gg.blit(texture, relX, relY, 0, 0, width, height, width, height * 2);
      int rWidth = (int) (width * Math.min(current / max, 1.0F));
      if (current != 0)
        gg.blit(texture, relX, relY, 0, height, rWidth, height, width, height * 2);
    }
  }

  public void renderHoveredToolTip(GuiGraphics gg, int mouseX, int mouseY, int curr) {
    if (this.isMouseover(mouseX, mouseY) && curr > 0) {
      String display;
      int seconds = curr / 20;
      if (curr > 20 * 60) {
        //if more than 120 secs which is two minutes
        int minutes = seconds / 60;
        int remainder = seconds % 60;
        display = minutes + "m " + remainder + "s";
      }
      else if (curr > 20 * 5) {
        //if more than 20 seconds, show seconds not ticks
        display = seconds + "s";
      }
      else {
        display = curr + "t";
      }
      List<Component> list = new ArrayList<>();
      list.add(Component.translatable(display));
      gg.renderComponentTooltip(Minecraft.getInstance().font, list, mouseX, mouseY);
    }
  }
}
