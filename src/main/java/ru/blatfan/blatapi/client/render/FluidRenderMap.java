package ru.blatfan.blatapi.client.render;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.Objects;

public class FluidRenderMap<V> extends Object2ObjectOpenCustomHashMap<FluidStack, V> {
  
  public enum FluidFlow {
    STILL, FLOWING
  }
  
  public FluidRenderMap() {
    super(FluidHashStrategy.INSTANCE);
  }
  
  @Nullable
  public static TextureAtlasSprite getFluidTexture(FluidStack fluidStack, FluidFlow type) {
    if (fluidStack == null || fluidStack.isEmpty()) {
      return getMissingTexture();
    }
    
    Fluid fluid = fluidStack.getFluid();
    IClientFluidTypeExtensions fluidAttributes = IClientFluidTypeExtensions.of(fluid);
    
    if (fluidAttributes == null) {
      return getMissingTexture();
    }
    
    ResourceLocation spriteLocation;
    try {
      if (type == FluidFlow.STILL) {
        spriteLocation = fluidAttributes.getStillTexture(fluidStack);
      } else {
        spriteLocation = fluidAttributes.getFlowingTexture(fluidStack);
      }
      
      if (spriteLocation == null) {
        return getMissingTexture();
      }
      
      return getSprite(spriteLocation);
    } catch (Exception e) {
      Minecraft.getInstance().getProfiler().popPush("fluidTextureError");
      return getMissingTexture();
    }
  }
  
  public static TextureAtlasSprite getSprite(ResourceLocation spriteLocation) {
    try {
      return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(spriteLocation);
    } catch (Exception e) {
      return getMissingTexture();
    }
  }
  
  public static TextureAtlasSprite getMissingTexture() {
    try {
      return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
          .apply(new ResourceLocation("minecraft:missingno"));
    } catch (Exception e) {
      return null;
    }
  }
  
  public static class FluidHashStrategy implements Hash.Strategy<FluidStack> {
    
    public static final FluidHashStrategy INSTANCE = new FluidHashStrategy();
    
    @Override
    public int hashCode(FluidStack stack) {
      if (stack == null || stack.isEmpty()) {
        return 0;
      }
      
      int code = stack.getFluid().hashCode();
      if (stack.hasTag()) {
        code = 31 * code + stack.getTag().hashCode();
      }
      return code;
    }
    
    @Override
    public boolean equals(FluidStack a, FluidStack b) {
      if (a == b) return true;
      if (a == null || b == null) return false;
      return a.isFluidEqual(b);
    }
  }
  
  public static TextureAtlasSprite getCachedFluidTexture(FluidStack fluidStack, FluidFlow type) {
    TextureCache.FluidTextureKey key = TextureCache.createKey(fluidStack, type);
    TextureAtlasSprite cached = TextureCache.get(key);
    if (cached != null)
      return cached;
    TextureAtlasSprite texture = getFluidTexture(fluidStack, type);
    if (texture != null)
      TextureCache.put(key, texture);
    return texture;
  }
}