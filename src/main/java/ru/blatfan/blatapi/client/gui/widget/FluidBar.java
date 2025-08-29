package ru.blatfan.blatapi.client.gui.widget;

import java.util.ArrayList;
import java.util.List;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.render.FluidRenderMap;

public class FluidBar {

  public ResourceLocation FLUID_WIDGET = BlatApi.loc("textures/gui/fluid.png");
  public String emtpyTooltip = "0";
  @Getter
  private Font font;
  @Getter
  private int x;
  @Getter
  private int y;
  @Getter
  private int capacity;
  @Getter@Setter
  private int width = 18;
  @Getter@Setter
  private int height = 62;
  public int guiLeft;
  public int guiTop;

  public FluidBar(Font p, int cap) {
    this(p, 132, 8, cap);
  }

  public FluidBar(Font p, int x, int y, int cap) {
    font = p;
    this.x = x;
    this.y = y;
    this.capacity = cap;
  }

  public void draw(GuiGraphics gg, FluidStack fluid) {
    final int u = 0, v = 0, x = guiLeft + getX(), y = guiTop + getY();
    gg.blit(FLUID_WIDGET,
        x, y, u, v,
        width, height,
        width, height);
    //NOW the fluid part
    if (fluid == null || this.getCapacity() == 0 || fluid.getAmount() == 0) {
      return;
    }
    float capacity = this.getCapacity();
    float amount = fluid.getAmount();
    float scale = amount / capacity;
    int fluidAmount = (int) (scale * height);
    TextureAtlasSprite sprite = FluidRenderMap.getCachedFluidTexture(fluid, FluidRenderMap.FluidFlow.STILL);
    if (fluid.getFluid() == Fluids.WATER) {
      RenderSystem.setShaderColor(0, 0, 1, 1);
    }
    int xPosition = x + 1;
    int yPosition = y + 1;
    int maximum = height - 2;
    int desiredWidth = width - 2;
    int desiredHeight = fluidAmount - 2;
    gg.blit(xPosition, yPosition + (maximum - desiredHeight), 0, desiredWidth, desiredHeight, sprite);
    if (fluid.getFluid() == Fluids.WATER) {
      RenderSystem.setShaderColor(1, 1, 1, 1);
    }
  }

  public boolean isMouseover(int mouseX, int mouseY) {
    return guiLeft + x <= mouseX && mouseX <= guiLeft + x + width
        && guiTop + y <= mouseY && mouseY <= guiTop + y + height;
  }

  public void renderHoveredToolTip(GuiGraphics ms, int mouseX, int mouseY, FluidStack current) {
    if (this.isMouseover(mouseX, mouseY)) {
      this.renderTooltip(ms, mouseX, mouseY, current);
    }
  }

  public void renderTooltip(GuiGraphics gg, int mouseX, int mouseY, FluidStack current) {
    String tt = emtpyTooltip;
    if (current != null && !current.isEmpty()) {
      tt = current.getAmount() + "/" + getCapacity() + " " + current.getDisplayName().getString();
    }
    List<Component> list = new ArrayList<>();
    list.add(Component.translatable(tt));
    gg.renderComponentTooltip(font, list, mouseX, mouseY);
  }
}
