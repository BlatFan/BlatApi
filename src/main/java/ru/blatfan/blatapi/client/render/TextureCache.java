package ru.blatfan.blatapi.client.render;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = "moracraft", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TextureCache {
    private static final Map<FluidTextureKey, WeakReference<TextureAtlasSprite>> CACHE = new ConcurrentHashMap<>();
    
    public static class FluidTextureKey {
        private final String fluidName;
        private final FluidRenderMap.FluidFlow type;
        private final int hashCode;
        
        private FluidTextureKey(String fluidName, FluidRenderMap.FluidFlow type) {
            this.fluidName = fluidName;
            this.type = type;
            this.hashCode = calculateHashCode();
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            
            FluidTextureKey other = (FluidTextureKey) obj;
            return fluidName.equals(other.fluidName) && type == other.type;
        }
        
        @Override
        public int hashCode() {
            return hashCode;
        }
        
        private int calculateHashCode() {
            int result = fluidName.hashCode();
            result = 31 * result + type.hashCode();
            return result;
        }
    }
    
    @Nullable
    public static TextureAtlasSprite get(FluidTextureKey key) {
        WeakReference<TextureAtlasSprite> ref = CACHE.get(key);
        return ref != null ? ref.get() : null;
    }
    
    public static void put(FluidTextureKey key, TextureAtlasSprite texture) {
        CACHE.put(key, new WeakReference<>(texture));
    }
    
    public static void clear() {
        CACHE.clear();
    }
    
    public static void cleanUp() {
        CACHE.entrySet().removeIf(entry -> {
            WeakReference<TextureAtlasSprite> ref = entry.getValue();
            return ref == null || ref.get() == null;
        });
    }
    
    public static int size() {
        return CACHE.size();
    }
    
    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Post event) {
        clear();
    }
    
    public static FluidTextureKey createKey(FluidStack fluidStack, FluidRenderMap.FluidFlow type) {
        String fluidName = BuiltInRegistries.FLUID.getKey(fluidStack.getFluid()).toString();
        return new FluidTextureKey(fluidName, type);
    }
}