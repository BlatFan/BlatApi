package ru.blatfan.blatapi.client.gui.widget;

import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.utils.collection.Text;

import java.util.ArrayList;
import java.util.List;

public class TexturedProgress {
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
  
  public static TexturedProgress createHorizontal(int x, int y) {
    return new TexturedProgress(x, y, 22, 16, BlatApi.guiLoc("progress_bar"));
  }
  public static TexturedProgress createVertical(int x, int y) {
    TexturedProgress vert = new TexturedProgress(x, y, 16, 22, BlatApi.guiLoc("progress_bar_vertical"));
    vert.setTopDown(true);
    return vert;
  }
  public static TexturedProgress create(int x, int y, ResourceLocation texture) {
    return new TexturedProgress(x, y, 22, 16, texture);
  }
  public static TexturedProgress create(int x, int y, int width, int height, ResourceLocation texture) {
    return new TexturedProgress(x, y, width, height, texture);
  }
  public static TexturedProgress create(int x, int y, int width, int height, boolean topDown, ResourceLocation texture) {
    TexturedProgress bar = new TexturedProgress(x, y, width, height, texture);
    bar.setTopDown(topDown);
    return bar;
  }

  protected TexturedProgress(int x, int y, int width, int height, ResourceLocation texture) {
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
    else {
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
      list.add(Text.create(display));
      gg.renderComponentTooltip(Minecraft.getInstance().font, list, mouseX, mouseY);
    }
  }
}
