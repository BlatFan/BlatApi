package ru.blatfan.blatapi.utils.gui_utils;

import com.mojang.blaze3d.vertex.PoseStack;
import lombok.experimental.UtilityClass;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraftforge.fluids.FluidStack;
import ru.blatfan.blatapi.client.registry.BARenderTypes;
import ru.blatfan.blatapi.client.render.FluidRenderMap;
import ru.blatfan.blatapi.client.render.RenderBuilder;

import java.awt.*;

@UtilityClass
@SuppressWarnings("ALL")
public class GuiFluidUtil {
    public static void renderFluid(PoseStack stack, FluidStack fluidStack, float size, float texSize, boolean flowing, int light) {
        renderFluid(stack, fluidStack, size, size, size, texSize, texSize, texSize, flowing, light);
    }
    
    public static void renderFluid(PoseStack stack, FluidStack fluidStack, float size, float texSize, Color color, boolean flowing, int light) {
        renderFluid(stack, fluidStack, size, size, size, texSize, texSize, texSize, color, flowing, light);
    }
    
    public static void renderFluid(PoseStack stack, FluidStack fluidStack, float width, float height, float length, float texWidth, float texHeight, float texLength, boolean flowing, int light) {
        if (!fluidStack.isEmpty()) {
            RenderBuilder builder = getFluidRenderBuilder(fluidStack, texWidth, texHeight, texLength, flowing, light);
            builder.renderCube(stack, width, height, length);
        }
    }
    
    public static void renderFluid(PoseStack stack, FluidStack fluidStack, float width, float height, float length, float texWidth, float texHeight, float texLength, Color color, boolean flowing, int light) {
        if (!fluidStack.isEmpty()) {
            RenderBuilder builder = getFluidRenderBuilder(fluidStack, texWidth, texHeight, texLength, flowing, light);
            builder.setColor(color).renderCube(stack, width, height, length);
        }
    }
    
    public static void renderCenteredFluid(PoseStack stack, FluidStack fluidStack, float size, float texSize, boolean flowing, int light) {
        renderCenteredFluid(stack, fluidStack, size, size, size, texSize, texSize, texSize, flowing, light);
    }
    
    public static void renderCenteredFluid(PoseStack stack, FluidStack fluidStack, float size, float texSize, Color color, boolean flowing, int light) {
        renderCenteredFluid(stack, fluidStack, size, size, size, texSize, texSize, texSize, color, flowing, light);
    }
    
    public static void renderCenteredFluid(PoseStack stack, FluidStack fluidStack, float width, float height, float length, float texWidth, float texHeight, float texLength, boolean flowing, int light) {
        if (!fluidStack.isEmpty()) {
            RenderBuilder builder = getFluidRenderBuilder(fluidStack, texWidth, texHeight, texLength, flowing, light);
            builder.renderCenteredCube(stack, width, height, length);
        }
    }
    
    public static void renderCenteredFluid(PoseStack stack, FluidStack fluidStack, float width, float height, float length, float texWidth, float texHeight, float texLength, Color color, boolean flowing, int light) {
        if (!fluidStack.isEmpty()) {
            RenderBuilder builder = getFluidRenderBuilder(fluidStack, texWidth, texHeight, texLength, flowing, light);
            builder.setColor(color).renderCenteredCube(stack, width, height, length);
        }
    }
    
    public static void renderWavyFluid(PoseStack stack, FluidStack fluidStack, float size, float texSize, boolean flowing, int light, float strength, float time) {
        renderWavyFluid(stack, fluidStack, size, size, size, texSize, texSize, texSize, flowing, light, strength, time);
    }
    
    public static void renderWavyFluid(PoseStack stack, FluidStack fluidStack, float size, float texSize, Color color, boolean flowing, int light, float strength, float time) {
        renderWavyFluid(stack, fluidStack, size, size, size, texSize, texSize, texSize, color, flowing, light, strength, time);
    }
    
    public static void renderWavyFluid(PoseStack stack, FluidStack fluidStack, float width, float height, float length, float texWidth, float texHeight, float texLength, boolean flowing, int light, float strength, float time) {
        if (!fluidStack.isEmpty()) {
            RenderBuilder builder = getFluidRenderBuilder(fluidStack, texWidth, texHeight, texLength, flowing, light);
            builder.renderWavyCube(stack, width, height, length, strength, time);
        }
    }
    
    public static void renderWavyFluid(PoseStack stack, FluidStack fluidStack, float width, float height, float length, float texWidth, float texHeight, float texLength, Color color, boolean flowing, int light, float strength, float time) {
        if (!fluidStack.isEmpty()) {
            RenderBuilder builder = getFluidRenderBuilder(fluidStack, texWidth, texHeight, texLength, flowing, light);
            builder.setColor(color).renderWavyCube(stack, width, height, length, strength, time);
        }
    }
    
    public static RenderBuilder getFluidRenderBuilder(FluidStack fluidStack, float texWidth, float texHeight, float texLength, boolean flowing, int light) {
        RenderBuilder builder = RenderBuilder.create().setRenderType(BARenderTypes.TRANSLUCENT_TEXTURE);
        if (!fluidStack.isEmpty()) {
            TextureAtlasSprite sprite = FluidRenderMap.getFluidTexture(fluidStack, flowing ? FluidRenderMap.FluidFlow.FLOWING : FluidRenderMap.FluidFlow.STILL);
            
            builder.setFirstUV(sprite.getU0(), sprite.getV0(), sprite.getU0() + ((sprite.getU1() - sprite.getU0()) * texLength), sprite.getV0() + ((sprite.getV1() - sprite.getV0()) * texWidth))
                .setSecondUV(sprite.getU0(), sprite.getV0(), sprite.getU0() + ((sprite.getU1() - sprite.getU0()) * texWidth), sprite.getV0() + ((sprite.getV1() - sprite.getV0()) * texHeight))
                .setThirdUV(sprite.getU0(), sprite.getV0(), sprite.getU0() + ((sprite.getU1() - sprite.getU0()) * texLength), sprite.getV0() + ((sprite.getV1() - sprite.getV0()) * texHeight))
                .setColor(FluidRenderMap.getTintColor(fluidStack))
                .setLight(Math.max(FluidRenderMap.getLightLevel(fluidStack) << 4, light));
        }
        return builder;
    }
    public static RenderBuilder getFluidRenderBuilder(FluidStack fluidStack, BlockAndTintGetter level, BlockPos pos, float texWidth, float texHeight, float texLength, boolean flowing, int light) {
        RenderBuilder builder = RenderBuilder.create().setRenderType(BARenderTypes.TRANSLUCENT_TEXTURE);
        if (!fluidStack.isEmpty()) {
            TextureAtlasSprite sprite = FluidRenderMap.getFluidTexture(fluidStack, flowing ? FluidRenderMap.FluidFlow.FLOWING : FluidRenderMap.FluidFlow.STILL);
            
            builder.setFirstUV(sprite.getU0(), sprite.getV0(), sprite.getU0() + ((sprite.getU1() - sprite.getU0()) * texLength), sprite.getV0() + ((sprite.getV1() - sprite.getV0()) * texWidth))
                .setSecondUV(sprite.getU0(), sprite.getV0(), sprite.getU0() + ((sprite.getU1() - sprite.getU0()) * texWidth), sprite.getV0() + ((sprite.getV1() - sprite.getV0()) * texHeight))
                .setThirdUV(sprite.getU0(), sprite.getV0(), sprite.getU0() + ((sprite.getU1() - sprite.getU0()) * texLength), sprite.getV0() + ((sprite.getV1() - sprite.getV0()) * texHeight))
                .setColor(FluidRenderMap.getTintColor(fluidStack, level, pos))
                .setLight(Math.max(FluidRenderMap.getLightLevel(fluidStack) << 4, light));
        }
        return builder;
    }
}
