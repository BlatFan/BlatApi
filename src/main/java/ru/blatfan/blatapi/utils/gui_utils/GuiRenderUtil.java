package ru.blatfan.blatapi.utils.gui_utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import ru.blatfan.blatapi.client.render.BlatApiRenderType;

import java.util.Optional;
import java.util.function.Supplier;

@UtilityClass
@SuppressWarnings("ALL")
public class GuiRenderUtil {
    public static final int FULL_BRIGHT = LightTexture.FULL_BRIGHT;
    
    public static float getPartialTick(){
        return Minecraft.getInstance().getPartialTick();
    }
    
    public static ShaderInstance getShader(RenderType type) {
        if (type instanceof BlatApiRenderType renderType) {
            Optional<Supplier<ShaderInstance>> shader = renderType.state.shaderState.shader;
            if (shader.isPresent())
                return shader.get().get();
        }
        return null;
    }
    
    public static void renderModel(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Model model, float red, float green, float blue, float alpha, RenderType renderType) {
        VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
        testColorValues(red, green, blue, alpha);
        model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
    }
    
    static void testColorValues(float red, float green, float blue, float alpha){
        if(red<0 || red>1) throw new IllegalArgumentException("0<=red<=1, red="+ red);
        if(green<0 || green>1) throw new IllegalArgumentException("0<=green<=1, green="+green);
        if(blue<0 || blue>1) throw new IllegalArgumentException("0<=blue<=1, blue="+blue);
        if(alpha<0 || alpha>1) throw new IllegalArgumentException("0<=alpha<=1, alpha="+alpha);
    }
    
    public static void renderCustomModel(ModelResourceLocation model, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getModelManager().getModel(model);
        Minecraft.getInstance().getItemRenderer().render(new ItemStack(Items.DIRT), displayContext, leftHand, poseStack, buffer, combinedLight, combinedOverlay, bakedmodel);
    }
    
    public static void renderBlockModel(ModelResourceLocation model, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        BakedModel bakedmodel = Minecraft.getInstance().getModelManager().getModel(model);
        Minecraft.getInstance().getItemRenderer().render(new ItemStack(Items.DIRT), displayContext, leftHand, poseStack, buffer, combinedLight, combinedOverlay, bakedmodel);
    }
}