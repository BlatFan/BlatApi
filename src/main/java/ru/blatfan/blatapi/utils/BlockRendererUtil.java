package ru.blatfan.blatapi.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import ru.blatfan.blatapi.fluffy_fur.client.event.ClientTickHandler;
import ru.blatfan.blatapi.fluffy_fur.client.render.RenderBuilder;
import ru.blatfan.blatapi.fluffy_fur.common.block.entity.BlockSimpleInventory;
import ru.blatfan.blatapi.fluffy_fur.registry.client.FluffyFurRenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;

import java.awt.*;
import java.util.List;

public class BlockRendererUtil {
    public static void dispatchTEToNearbyPlayers(BlockEntity tile) {
        ServerLevel world = (ServerLevel) tile.getLevel();
        world.getChunkSource().chunkMap.getPlayers(new ChunkPos(tile.getBlockPos()), false)
            .forEach(player -> player.connection.send(tile.getUpdatePacket()));
    }
    public static void dispatchTEToNearbyPlayers(Level world, BlockPos pos) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile != null) dispatchTEToNearbyPlayers(tile);
    }
    public static void renderItem(BlockEntity block, double x, double y, double z, List<Float> size, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1, ItemStack itemStack){
        renderItem(block.getBlockPos(), x, y, z, size, poseStack, multiBufferSource, i, i1, itemStack);
    }
    public static void renderItem(BlockPos pos, double x, double y, double z, List<Float> size, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1, ItemStack itemStack){
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        float scale = itemStack.getItem() instanceof BlockItem ? size.get(0) : size.get(1);
        poseStack.scale(scale, scale, scale);
        double tick = System.currentTimeMillis() / 800.0D;
        poseStack.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
        poseStack.mulPose(Axis.YP.rotationDegrees((float) ((tick * 40.0D) % 360)));
        if(itemStack.getItem() instanceof ParticleItem item)
            item.worldParticles((float) (pos.getX()+x), (float) ((pos.getY()+y)+Math.sin(tick % (2 * Math.PI)) * 0.065D), (float) (pos.getZ()+z));
        Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemDisplayContext.GROUND, i,
            i1, poseStack, multiBufferSource, Minecraft.getInstance().level, itemStack.getDescriptionId().length());
        poseStack.popPose();
    }
    
    public static void renderItemWithDragon(BlockEntity block, double x, double y, double z, List<Float> size, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1, ItemStack itemStack, Color color){
        renderItemWithDragon(block.getBlockPos(), x, y, z, size, poseStack, multiBufferSource, i, i1, itemStack, color);
    }
    public static void renderItemWithDragon(BlockPos pos, double x, double y, double z, List<Float> size, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1, ItemStack itemStack, Color color){
        renderItem(pos, x, y, z, size, poseStack, multiBufferSource, i, i1, itemStack);
        double tick = System.currentTimeMillis() / 800.0D;
        RenderBuilder.create()
            .setRenderType(FluffyFurRenderTypes.ADDITIVE)
            .replaceBufferSource(multiBufferSource)
            .setAlpha(0.5f)
            .setColor(color)
            .renderDragon(poseStack, x, Math.sin(tick % (2 * Math.PI))* 0.065D + y+0.15, z,
                1f,
                ClientTickHandler.partialTicks, itemStack.getDescriptionId().length());
    }
    
    public static BaseItemStackHandler getItemHandler(BlockSimpleInventory blockSimpleInventory){
        BaseItemStackHandler itemStackHandler = new BaseItemStackHandler(blockSimpleInventory.getItemHandler().getContainerSize(), () -> {});
        for(int i=0; i<blockSimpleInventory.getItemHandler().getContainerSize(); i++)
            itemStackHandler.setStackInSlot(i, blockSimpleInventory.getItemHandler().getItem(i));
        return  itemStackHandler;
    }
    public static LazyOptional<BaseItemStackHandler> getLazyItems(BlockSimpleInventory blockSimpleInventory){
        return LazyOptional.of(() -> getItemHandler(blockSimpleInventory));
    }
}
