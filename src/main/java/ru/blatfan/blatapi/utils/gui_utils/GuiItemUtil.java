package ru.blatfan.blatapi.utils.gui_utils;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import lombok.experimental.UtilityClass;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.client.ForgeHooksClient;
import org.joml.Matrix4f;
import ru.blatfan.blatapi.client.render.item.CustomItemRenderer;
import ru.blatfan.blatapi.utils.BAGeckoHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static ru.blatfan.blatapi.utils.gui_utils.GuiRenderUtil.*;
import static ru.blatfan.blatapi.utils.gui_utils.GuiTextUtil.getFont;
import static ru.blatfan.blatapi.utils.gui_utils.GuiTextUtil.renderScaledTooltip;

@UtilityClass
@SuppressWarnings("ALL")
public class GuiItemUtil {
    private static CustomItemRenderer customItemRenderer;
    
    public static void renderScaledIngredient(GuiGraphics gui, int x, int y, Ingredient ingredient, float scale){
        ItemStack[] stacks = ingredient.getItems();
        if(stacks.length==0) return;
        int index = (int) (System.currentTimeMillis() / 1000 % stacks.length);
        renderScaledItem(gui, stacks[index], x, y, scale);
        renderScaledItemDecorations(gui, stacks[index], x, y, scale);
    }
    public static void renderIngredient(GuiGraphics gui, int x, int y, Ingredient ingredient){
        renderScaledIngredient(gui, x,y, ingredient, 1);
    }
    public static void renderScaledItem(GuiGraphics gui, ItemStack pStack, int x, int y, float size) {
        gui.pose().pushPose();
        gui.pose().scale(size, size, 1);
        gui.renderItem(pStack, (int) (x/size), (int) (y/size));
        gui.pose().popPose();
    }
    public static void renderScaledFakeItem(GuiGraphics gui, ItemStack pStack, int x, int y, float size) {
        gui.pose().pushPose();
        gui.pose().scale(size, size, 1);
        gui.renderFakeItem(pStack, (int) (x/size), (int) (y/size));
        gui.pose().popPose();
    }
    public static void renderScaledItemDecorations(GuiGraphics gui, ItemStack pStack, int x, int y, float size) {
        gui.pose().pushPose();
        gui.pose().scale(size, size, 1);
        gui.renderItemDecorations(getFont(), pStack, (int) (x/size), (int) (y/size));
        gui.pose().popPose();
    }
    
    public static void renderTooltip(GuiGraphics gui, int x, int y, Ingredient ingredient, float scale){
        ItemStack[] stacks = ingredient.getItems();
        if(stacks.length==0) return;
        int index = (int) (System.currentTimeMillis() / 1000 % stacks.length);
        List<net.minecraft.network.chat.Component> components = new ArrayList<>(Screen.getTooltipFromItem(Minecraft.getInstance(), stacks[index]));
        if(ingredient.values[0] instanceof Ingredient.TagValue tagValue && Minecraft.getInstance().options.advancedItemTooltips)
            components.add(1, Component.literal("Tag: #"+ tagValue.tag.location()).withStyle(ChatFormatting.DARK_GRAY));
        renderScaledTooltip(gui, stacks[index], components, x, y, scale);
    }
    public static void renderTooltip(GuiGraphics gui, int x, int y, Ingredient ingredient){
        renderTooltip(gui, x,y, ingredient, 1);
    }
    
    public static CustomItemRenderer getCustomItemRenderer() {
        Minecraft minecraft = Minecraft.getInstance();
        if (customItemRenderer == null) customItemRenderer = new CustomItemRenderer(minecraft, minecraft.getTextureManager(), minecraft.getModelManager(), minecraft.getItemColors(), minecraft.getItemRenderer().getBlockEntityRenderer());
        return customItemRenderer;
    }
    
    public static void renderItemModelInGui(ItemStack stack, float x, float y, float xSize, float ySize, float zSize) {
        renderItemModelInGui(stack, x, y, xSize, ySize, zSize, 0, 0, 0, FULL_BRIGHT);
    }
    
    public static void renderItemModelInGui(ItemStack stack, float x, float y, float xSize, float ySize, float zSize, float xRot, float yRot, float zRot, int packedLight) {
        renderItemModelInGui(stack, x, y, 100, xSize, ySize, zSize, xRot, yRot, zRot, packedLight);
    }
    public static void renderItemModelInGui(ItemStack stack, float x, float y, float z, float xSize, float ySize, float zSize, float xRot, float yRot, float zRot, int packedLight) {
        Minecraft minecraft = Minecraft.getInstance();
        BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(stack, minecraft.level, minecraft.player, 0);
        CustomItemRenderer customItemRenderer = getCustomItemRenderer();
        
        PoseStack poseStack = RenderSystem.getModelViewStack();
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        poseStack.translate((double) xSize / 2, (double) ySize / 2, 0);
        poseStack.scale(1, -1, 1);
        poseStack.scale(xSize, ySize, zSize);
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.ZP.rotationDegrees(zRot));
        RenderSystem.applyModelViewMatrix();
        PoseStack pose = new PoseStack();
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        boolean flag = !bakedmodel.usesBlockLight();
        if (flag) Lighting.setupForFlatItems();
        
        customItemRenderer.render(stack, ItemDisplayContext.GUI, false, pose, multibuffersource$buffersource, packedLight, OverlayTexture.NO_OVERLAY, bakedmodel);
        
        RenderSystem.disableDepthTest();
        multibuffersource$buffersource.endBatch();
        RenderSystem.enableDepthTest();
        if (flag) Lighting.setupFor3DItems();
        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
    }
    
    public static void renderFloatingItemModelIntoGUI(GuiGraphics gui, ItemStack stack, float x, float y, int packedLight, float ticks, float ticksUp) {
        renderFloatingItemModelIntoGUI(gui, stack, x, y, 150, packedLight, ticks, ticksUp);
    }
    public static void renderFloatingItemModelIntoGUI(GuiGraphics gui, ItemStack stack, float x, float y, float z, int packedLight, float ticks, float ticksUp) {
        Minecraft minecraft = Minecraft.getInstance();
        BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(stack, minecraft.level, minecraft.player, 0);
        CustomItemRenderer customItemRenderer = getCustomItemRenderer();
        
        float old = bakedmodel.getTransforms().gui.rotation.y;
        PoseStack pose = gui.pose();
        
        pose.pushPose();
        pose.translate(x + 8, y + 8, z);
        pose.mulPoseMatrix((new Matrix4f()).scaling(1, -1, 1));
        pose.scale(16, 16, 16);
        pose.translate(0, Math.sin(Math.toRadians(ticksUp)) * 0.03125F, 0);
        if (bakedmodel.usesBlockLight()) {
            bakedmodel.getTransforms().gui.rotation.y = ticks;
        } else {
            pose.mulPose(Axis.YP.rotationDegrees(ticks));
        }
        boolean flag = !bakedmodel.usesBlockLight();
        if (flag) Lighting.setupForFlatItems();
        
        customItemRenderer.renderItem(stack, ItemDisplayContext.GUI, false, pose, Minecraft.getInstance().renderBuffers().bufferSource(), packedLight, OverlayTexture.NO_OVERLAY, bakedmodel);
        
        RenderSystem.disableDepthTest();
        Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
        RenderSystem.enableDepthTest();
        if (flag) Lighting.setupFor3DItems();
        pose.popPose();
        RenderSystem.applyModelViewMatrix();
        
        bakedmodel.getTransforms().gui.rotation.y = old;
    }
    
    public static void renderArmorInGui(GuiGraphics gui, ItemStack stack, int x, int y, int z, float sizeX, float sizeY, float sizeZ,
                                        float xRot, float yRot, float zRot, int packedLight) {
        renderArmorInGui(gui, stack, x, y, z, sizeX, sizeY, sizeZ, xRot, yRot, zRot, packedLight, 1, 1, 1, 1);
    }
    public static void renderArmorInGui(GuiGraphics gui, ItemStack stack, int x, int y, int z, float sizeX, float sizeY, float sizeZ,
                                        float xRot, float yRot, float zRot, int packedLight, float red, float blue, float green, float alpha) {
        if(!(stack.getItem() instanceof ArmorItem armorItem)) return;
        Minecraft mc = Minecraft.getInstance();
        EquipmentSlot slot = LivingEntity.getEquipmentSlotForItem(stack);
        
        EntityRendererProvider.Context context = new EntityRendererProvider.Context(mc.getEntityRenderDispatcher(), mc.getItemRenderer(),
            mc.getBlockRenderer(), mc.getEntityRenderDispatcher().getItemInHandRenderer(), mc.getResourceManager(),
            mc.getEntityModels(), mc.font);
        ModelPart playerModel = slot==EquipmentSlot.LEGS ?
            context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR) : context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR);
        Model model = ForgeHooksClient.getArmorModel(mc.player, stack, slot, new HumanoidArmorModel<>(playerModel));
        if(model instanceof HumanoidModel<?> humanoidModel){
            humanoidModel.head.visible = slot==EquipmentSlot.HEAD;
            humanoidModel.hat.visible = slot==EquipmentSlot.HEAD;
            humanoidModel.body.visible = slot==EquipmentSlot.CHEST;
            humanoidModel.leftArm.visible = slot==EquipmentSlot.CHEST;
            humanoidModel.rightArm.visible = slot==EquipmentSlot.CHEST;
            humanoidModel.rightLeg.visible = slot==EquipmentSlot.LEGS || slot==EquipmentSlot.FEET;
            humanoidModel.leftLeg.visible = slot==EquipmentSlot.LEGS || slot==EquipmentSlot.FEET;
            humanoidModel.young=false;
        }
        
        int yOffset = Integer.MAX_VALUE;
        if(BAGeckoHelper.isGeckoArmor(stack)) yOffset= BAGeckoHelper.getGeckoArmorOffset(sizeY, slot);
        if(yOffset==Integer.MAX_VALUE) yOffset = (int) (-sizeY+(sizeY/16)*switch(slot){
            case HEAD -> 4;
            case CHEST -> 14;
            case LEGS -> 24;
            case FEET -> 28;
            default -> 0;
        });
        
        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.translate(x+(sizeX/2), y-yOffset, z);
        pose.scale(sizeX, sizeY, sizeZ);
        pose.mulPose(Axis.XP.rotationDegrees(xRot));
        pose.mulPose(Axis.YP.rotationDegrees(yRot));
        pose.mulPose(Axis.ZP.rotationDegrees(zRot));
        
        RenderSystem.enableDepthTest();
        Lighting.setupForFlatItems();
        
        MultiBufferSource.BufferSource bufferSource = gui.bufferSource();
        
        if (armorItem instanceof DyeableLeatherItem) {
            Color color = new Color(((DyeableLeatherItem)armorItem).getColor(stack));
            renderModel(pose, bufferSource, packedLight, model,
                color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1f,
                RenderType.armorCutoutNoCull(getArmorResource(mc.player, stack, slot, null)));
            renderModel(pose, bufferSource, packedLight, model, red, green, blue, alpha,
                RenderType.armorCutoutNoCull(getArmorResource(mc.player, stack, slot, "overlay")));
        } else {
            renderModel(pose, bufferSource, packedLight, model, red, green, blue, alpha,
                RenderType.armorCutoutNoCull(getArmorResource(mc.player, stack, slot, null)));
        }
        
        ArmorTrim.getTrim(mc.player.level().registryAccess(), stack).ifPresent((trim) ->
            renderTrim(armorItem.getMaterial(), pose, bufferSource, packedLight, trim, model));
        if(stack.hasFoil())
            renderGlint(pose, bufferSource, RenderType.armorEntityGlint(), packedLight, model, red, green, blue, alpha);
        
        bufferSource.endBatch();
        Lighting.setupFor3DItems();
        RenderSystem.disableDepthTest();
        
        pose.popPose();
    }
    
    public static void renderGlint(PoseStack poseStack, MultiBufferSource buffer, RenderType renderType, int packedLight, Model model,
                                   float red, float green, float blue, float alpha) {
        testColorValues(red, green, blue, alpha);
        model.renderToBuffer(poseStack, buffer.getBuffer(renderType), packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
    }
    
    public static ResourceLocation getArmorResource(Entity entity, ItemStack stack, EquipmentSlot slot, String type) {
        Minecraft mc = Minecraft.getInstance();
        HumanoidArmorLayer layer = new HumanoidArmorLayer(null, null, null, mc.getModelManager());
        return layer.getArmorResource(mc.player, stack, slot, type);
    }
    
    public static void renderTrim(ArmorMaterial armorMaterial, PoseStack poseStack, MultiBufferSource buffer, int packedLight, ArmorTrim trim, Model model) {
        renderTrim(armorMaterial, poseStack, buffer, packedLight, trim, model, 1, 1, 1, 1);
    }
    public static void renderTrim(ArmorMaterial armorMaterial, PoseStack poseStack, MultiBufferSource buffer, int packedLight, ArmorTrim trim, Model model,
                                  float red, float green, float blue, float alpha) {
        TextureAtlas armorTrimAtlas = Minecraft.getInstance().getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET);
        TextureAtlasSprite textureatlassprite = armorTrimAtlas.getSprite(trim.outerTexture(armorMaterial));
        VertexConsumer vertexconsumer = textureatlassprite.wrap(buffer.getBuffer(Sheets.armorTrimsSheet()));
        model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
    }
}