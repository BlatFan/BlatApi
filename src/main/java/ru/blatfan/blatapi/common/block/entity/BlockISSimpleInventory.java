package ru.blatfan.blatapi.common.block.entity;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;
import ru.blatfan.blatapi.utils.BaseItemStackHandler;

import javax.annotation.Nonnull;

@Getter
public abstract class BlockISSimpleInventory extends BlockEntityBase {
    private boolean isChanged = false;
    private final BaseItemStackHandler itemHandler = this.createItemHandler();
    
    protected abstract BaseItemStackHandler createItemHandler();
    
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(pTag);
    }
    
    @Override
    public void load(CompoundTag pTag) {
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        setChanged();
        super.load(pTag);
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return LazyOptional.of(this::getItemHandler).cast();
        
        return super.getCapability(cap, side);
    }
    
    public void setChanged() {
        super.setChanged();
        this.isChanged = true;
    }
    
    public BlockISSimpleInventory(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }
}
