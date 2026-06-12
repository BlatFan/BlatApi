package ru.blatfan.blatapi.client.render;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import ru.blatfan.blatapi.utils.gui_utils.GuiTextureUtil;

import javax.annotation.Nullable;
import java.awt.*;

public class FluidRenderMap<V> extends Object2ObjectOpenCustomHashMap<FluidStack, V> {
  public FluidRenderMap() {
    super(FluidHashStrategy.INSTANCE);
  }
  
  public static Color getTintColor(FluidStack fluidStack){
    if (fluidStack == null || fluidStack.isEmpty()) return Color.WHITE;
    
    Fluid fluid = fluidStack.getFluid();
    IClientFluidTypeExtensions fluidAttributes = IClientFluidTypeExtensions.of(fluid);
    
    if (fluidAttributes == null) return Color.WHITE;
    return new Color(fluidAttributes.getTintColor());
  }
  public static Color getTintColor(FluidStack fluidStack, BlockAndTintGetter level, BlockPos pos){
    if (fluidStack == null || fluidStack.isEmpty()) return Color.WHITE;
    
    Fluid fluid = fluidStack.getFluid();
    IClientFluidTypeExtensions fluidAttributes = IClientFluidTypeExtensions.of(fluid);
    
    if (fluidAttributes == null) return Color.WHITE;
    return new Color(fluidAttributes.getTintColor(fluidStack.getFluid().defaultFluidState(), level, pos));
  }
  
  public static int getLightLevel(FluidStack fluidStack){
    if (fluidStack == null || fluidStack.isEmpty())
      return Fluids.WATER.getFluidType().getLightLevel();
    return fluidStack.getFluid().getFluidType().getLightLevel(fluidStack);
  }
  
  
  @Nullable
  public static TextureAtlasSprite getFluidTexture(Fluid fluid){
    return getFluidTexture(new FluidStack(fluid, 1000), FluidFlow.STILL);
  }
  
  @Nullable
  public static TextureAtlasSprite getFluidTexture(FluidStack fluidStack, FluidFlow type) {
    if (fluidStack == null || fluidStack.isEmpty()) {
      return GuiTextureUtil.getMissingTexture();
    }
    
    Fluid fluid = fluidStack.getFluid();
    IClientFluidTypeExtensions fluidAttributes = IClientFluidTypeExtensions.of(fluid);
    
    if (fluidAttributes == null) {
      return GuiTextureUtil.getMissingTexture();
    }
    
    ResourceLocation spriteLocation;
    try {
      if (type == FluidFlow.STILL) {
        spriteLocation = fluidAttributes.getStillTexture(fluidStack);
      } else {
        spriteLocation = fluidAttributes.getFlowingTexture(fluidStack);
      }
      
      if (spriteLocation == null) {
        return GuiTextureUtil.getMissingTexture();
      }
      
      return GuiTextureUtil.getSprite(spriteLocation);
    } catch (Exception e) {
      Minecraft.getInstance().getProfiler().popPush("fluidTextureError");
      return GuiTextureUtil.getMissingTexture();
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
  
  public enum FluidFlow {
    STILL, FLOWING
  }
}