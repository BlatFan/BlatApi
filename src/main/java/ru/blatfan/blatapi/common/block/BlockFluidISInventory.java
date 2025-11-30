package ru.blatfan.blatapi.common.block;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;
import ru.blatfan.blatapi.utils.BaseItemStackHandler;

import javax.annotation.Nonnull;

@Getter
public abstract class BlockFluidISInventory extends BlockEntity {
    private boolean changed = false;
    private final BaseItemStackHandler itemHandler = this.createItemHandler();
    private final FluidTank fluidTank = this.createFluidTank();
    
    public BlockFluidISInventory(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }
    
    protected abstract BaseItemStackHandler createItemHandler();
    protected abstract FluidTank createFluidTank();
    
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::saveWithFullMetadata);
    }
    
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.put("fluidTank", fluidTank.writeToNBT(new CompoundTag()));
        super.saveAdditional(pTag);
    }
    
    @Override
    public void load(CompoundTag pTag) {
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        fluidTank.readFromNBT(pTag.getCompound("fluidTank"));
        setChanged();
        super.load(pTag);
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return LazyOptional.of(this::getItemHandler).cast();
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return LazyOptional.of(this::getFluidTank).cast();
        return super.getCapability(cap, side);
    }
    
    @Override
    public void setChanged() {
        super.setChanged();
        this.changed = true;
    }
}
