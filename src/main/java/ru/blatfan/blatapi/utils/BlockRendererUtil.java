package ru.blatfan.blatapi.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import ru.blatfan.blatapi.fluffy_fur.client.event.ClientTickHandler;
import ru.blatfan.blatapi.fluffy_fur.client.render.RenderBuilder;
import ru.blatfan.blatapi.fluffy_fur.common.block.entity.BlockSimpleInventory;
import ru.blatfan.blatapi.fluffy_fur.registry.client.FluffyFurRenderTypes;

import java.awt.*;
import java.util.List;

import static ru.blatfan.blatapi.utils.BlockUtil.getItemY;

@UtilityClass
public class BlockRendererUtil {
    /**
     * use {@link BlockUtil#dispatchTEToNearbyPlayers(BlockEntity)}
     */
    @Deprecated(forRemoval = true, since = "0.3.1")
    public static void dispatchTEToNearbyPlayers(BlockEntity tile) {
        ServerLevel world = (ServerLevel) tile.getLevel();
        world.getChunkSource().chunkMap.getPlayers(new ChunkPos(tile.getBlockPos()), false)
            .forEach(player -> player.connection.send(tile.getUpdatePacket()));
    }
    /**
     * use {@link BlockUtil#dispatchTEToNearbyPlayers(Level, BlockPos)}
     */
    @Deprecated(forRemoval = true, since = "0.3.1")
    public static void dispatchTEToNearbyPlayers(Level world, BlockPos pos) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile != null) dispatchTEToNearbyPlayers(tile);
    }
    /**
     * use {@link BlockUtil#renderItem(BlockPos, double, double, double, List, PoseStack, MultiBufferSource, int, int, ItemStack)}
     */
    @Deprecated(forRemoval = true, since = "0.3.1")
    public static void renderItem(BlockEntity block, double x, double y, double z, List<Float> size, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1, ItemStack itemStack){
        renderItem(block.getBlockPos(), x, y, z, size, poseStack, multiBufferSource, i, i1, itemStack);
    }
    
    /**
     * use {@link BlockUtil#getItemSpin()}
     */
    @Deprecated(forRemoval = true, since = "0.3.1")
    public static double getItemSpin(){
        double tick;
        if(Minecraft.getInstance().level==null) tick = System.currentTimeMillis() / 800d;
        else tick = Minecraft.getInstance().level.getGameTime() / 16d;
        return (tick * 40.0D) % 360;
    }
    
    /**
     * use {@link BlockUtil#renderItem(BlockEntity, double, double, double, List, PoseStack, MultiBufferSource, int, int, ItemStack)}
     */
    @Deprecated(forRemoval = true, since = "0.3.1")
    public static void renderItem(BlockPos pos, double x, double y, double z, List<Float> size, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1, ItemStack itemStack){
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        float scale = itemStack.getItem() instanceof BlockItem ? size.get(0) : size.get(1);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0D, getItemSpin(), 0.0D);
        poseStack.mulPose(Axis.YP.rotationDegrees((float) getItemSpin()));
        if(itemStack.getItem() instanceof ParticleItem item)
            item.worldParticles((float) (pos.getX()+x), (float) ((pos.getY()+y)+getItemY()), (float) (pos.getZ()+z));
        Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemDisplayContext.GROUND, i,
            i1, poseStack, multiBufferSource, Minecraft.getInstance().level, itemStack.getDescriptionId().length());
        poseStack.popPose();
    }
    
    /**
     * use {@link BlockUtil#renderItemWithDragon(BlockEntity, double, double, double, List, PoseStack, MultiBufferSource, int, int, ItemStack, Color)}
     */
    @Deprecated(forRemoval = true, since = "0.3.1")
    public static void renderItemWithDragon(BlockEntity block, double x, double y, double z, List<Float> size, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1, ItemStack itemStack, Color color){
        renderItemWithDragon(block.getBlockPos(), x, y, z, size, poseStack, multiBufferSource, i, i1, itemStack, color);
    }
    
    /**
     * use {@link BlockUtil#renderItem(BlockPos, double, double, double, List, PoseStack, MultiBufferSource, int, int, ItemStack)}
     */
    @Deprecated(forRemoval = true, since = "0.3.1")
    public static void renderItemWithDragon(BlockPos pos, double x, double y, double z, List<Float> size, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1, ItemStack itemStack, Color color){
        renderItem(pos, x, y, z, size, poseStack, multiBufferSource, i, i1, itemStack);
        RenderBuilder.create()
            .setRenderType(FluffyFurRenderTypes.ADDITIVE)
            .replaceBufferSource(multiBufferSource)
            .setAlpha(0.5f)
            .setColor(color)
            .renderDragon(poseStack, x, getItemY() + y+0.15, z,
                1f,
                ClientTickHandler.partialTicks, itemStack.getDescriptionId().length());
    }
    
    /**
     * use {@link BlockUtil#getItemHandler(BlockSimpleInventory)}
     */
    @Deprecated(forRemoval = true, since = "0.3.1")
    public static BaseItemStackHandler getItemHandler(BlockSimpleInventory blockSimpleInventory){
        BaseItemStackHandler itemStackHandler = new BaseItemStackHandler(blockSimpleInventory.getItemHandler().getContainerSize(), () -> {});
        for(int i=0; i<blockSimpleInventory.getItemHandler().getContainerSize(); i++)
            itemStackHandler.setStackInSlot(i, blockSimpleInventory.getItemHandler().getItem(i));
        return itemStackHandler;
    }
    
    /**
     * use {@link BlockUtil#getLazyItems(BlockSimpleInventory)}
     */
    @Deprecated(forRemoval = true, since = "0.3.1")
    public static LazyOptional<BaseItemStackHandler> getLazyItems(BlockSimpleInventory blockSimpleInventory){
        return LazyOptional.of(() -> getItemHandler(blockSimpleInventory));
    }
    
    /**
     * use {@link BlockUtil#getLazyItems(BlockSimpleInventory)}
     */
    @Deprecated(forRemoval = true, since = "0.3.1")
    public static LazyOptional<IItemHandler> getLazyItems(BlockEntity blockEntity){
        return LazyOptional.of(() -> blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(new BaseItemStackHandler(0, blockEntity::setChanged)));
    }
}