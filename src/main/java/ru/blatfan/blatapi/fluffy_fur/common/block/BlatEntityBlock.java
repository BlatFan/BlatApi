package ru.blatfan.blatapi.fluffy_fur.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class BlatEntityBlock extends BaseEntityBlock {
    private final BiFunction<BlockPos, BlockState, BlockEntity> blockEntityBuilder;
    public BlatEntityBlock(Properties pProperties, BiFunction<BlockPos, BlockState, BlockEntity> blockEntityBuilder) {
        super(pProperties);
        this.blockEntityBuilder = blockEntityBuilder;
    }
    
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(handler -> {
                SimpleContainer container = new SimpleContainer(handler.getSlots());
                for(int i=0; i<container.getContainerSize(); i++)
                    container.setItem(i, handler.getStackInSlot(i));
                Containers.dropContents(level, pos, container);
            });
        }
        
        super.onRemove(state, level, pos, newState, isMoving);
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return blockEntityBuilder.apply(pPos, pState);
    }
}
