package ru.blatfan.blatapi.render.type;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class OverlayRenderType extends RenderType {

  public OverlayRenderType(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
    super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
  }

  public static RenderType overlayRenderer(String id, ResourceLocation resourceLocation) {
    RenderType.CompositeState state = RenderType.CompositeState.builder()
        .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false))
        .setCullState(RenderStateShard.NO_CULL)
        .setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER)
        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
        .setOutputState(RenderStateShard.TRANSLUCENT_TARGET)
        .createCompositeState(true);
    return create(id, DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, true, false, state);
  }
}
