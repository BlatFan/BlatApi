package ru.blatfan.blatapi.client.render;

import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class BERenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
    protected final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    protected final BlockRenderDispatcher blockRenderDispatcher;
    protected final ItemRenderer itemRenderer;
    protected final EntityRenderDispatcher entityRenderer;
    protected final EntityModelSet modelSet;
    protected final Font font;
    
    protected BERenderer(BlockEntityRendererProvider.Context context) {
        this.blockEntityRenderDispatcher = context.getBlockEntityRenderDispatcher();
        this.blockRenderDispatcher = context.getBlockRenderDispatcher();
        this.itemRenderer = context.getItemRenderer();
        this.entityRenderer = context.getEntityRenderer();
        this.modelSet = context.getModelSet();
        this.font = context.getFont();
    }
}