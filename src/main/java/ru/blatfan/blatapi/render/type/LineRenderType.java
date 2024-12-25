package ru.blatfan.blatapi.render.type;

import java.util.OptionalDouble;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import net.minecraft.client.renderer.RenderType;

public class LineRenderType extends RenderType {

  public LineRenderType(String nameIn, VertexFormat formatIn, Mode drawMode, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
    super(nameIn, formatIn, drawMode, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
  }

  public static RenderType tombLinesType() {
    return create("tomb_lines",
        DefaultVertexFormat.POSITION_COLOR, Mode.LINES, 256, false, false,
        RenderType.CompositeState.builder()
            .setShaderState(RENDERTYPE_LINES_SHADER)
            .setLineState(new LineStateShard(OptionalDouble.of(2.5D)))
            .setLayeringState(VIEW_OFFSET_Z_LAYERING)
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setOutputState(ITEM_ENTITY_TARGET)
            .setWriteMaskState(COLOR_DEPTH_WRITE)
            .setCullState(NO_CULL)
            .setDepthTestState(NO_DEPTH_TEST)
            .createCompositeState(false));
  }
}
