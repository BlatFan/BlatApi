package ru.blatfan.blatapi.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.utils.collection.Couple;

import java.util.IdentityHashMap;
import java.util.Map;

public class GhostBuffers extends MultiBufferSource.BufferSource {
    private final float alpha;
    private GhostBuffers(BufferBuilder fallback, Map<RenderType, BufferBuilder> layerBuffers, float alpha) {
        super(fallback, layerBuffers);
        this.alpha = alpha;
    }
    
    public static GhostBuffers create(MultiBufferSource.BufferSource original, float alpha){
        Map<RenderType, BufferBuilder> remapped = new Object2ObjectLinkedOpenHashMap<>();
        for (Map.Entry<RenderType, BufferBuilder> e : original.fixedBuffers.entrySet())
            remapped.put(GhostBuffers.GhostRenderLayer.remap(e.getKey(), alpha), e.getValue());
        return new GhostBuffers(original.builder, remapped, alpha);
    }
    
    @Override
    public VertexConsumer getBuffer(RenderType type) {
        return super.getBuffer(GhostRenderLayer.remap(type, alpha));
    }
    
    private static class GhostRenderLayer extends RenderType {
        private static final Map<Couple<RenderType, Float>, RenderType> remappedTypes = new IdentityHashMap<>();
        
        GhostRenderLayer(RenderType original, float alpha) {
            super(String.format("%s_%s_ghost", original.toString(), BlatApi.MOD_ID), original.format(), original.mode(), original.bufferSize(), original.affectsCrumbling(), true, () -> {
                original.setupRenderState();
                
                RenderSystem.enableBlend();
                RenderSystem.setShaderColor(1, 1, 1, alpha);
            }, () -> {
                RenderSystem.setShaderColor(1, 1, 1, 1);
                RenderSystem.disableBlend();
                
                original.clearRenderState();
            });
        }
        GhostRenderLayer(Couple<RenderType, Float> data) {
            this(data.getKey(), data.getValue());
        }
        
        public static RenderType remap(RenderType in, float alpha) {
            if (in instanceof GhostRenderLayer) {
                return in;
            } else {
                return remappedTypes.computeIfAbsent(new Couple<>(in, alpha), GhostRenderLayer::new);
            }
        }
    }
}