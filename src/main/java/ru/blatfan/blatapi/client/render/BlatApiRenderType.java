package ru.blatfan.blatapi.client.render;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

public class BlatApiRenderType extends RenderType {
    public final CompositeState state;

    public static BlatApiRenderType createRenderType(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, CompositeState state) {
        return new BlatApiRenderType(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, state);
    }

    public BlatApiRenderType(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, CompositeState state) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, () -> {
            state.states.forEach(RenderStateShard::setupRenderState);
        }, () -> {
            state.states.forEach(RenderStateShard::clearRenderState);
        });
        this.state = state;
    }
}
