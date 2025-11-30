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

import javax.annotation.Nonnull;

@Getter
public abstract class BlockFluidTank extends BlockEntity {
    private boolean changed = false;
    private final FluidTank fluidTank = this.createFluidTank();
    
    public BlockFluidTank(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }
    
    protected abstract FluidTank createFluidTank();
    
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::saveWithFullMetadata);
    }
    
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("fluidTank", fluidTank.writeToNBT(new CompoundTag()));
        super.saveAdditional(pTag);
    }
    
    @Override
    public void load(CompoundTag pTag) {
        fluidTank.readFromNBT(pTag.getCompound("fluidTank"));
        setChanged();
        super.load(pTag);
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return LazyOptional.of(this::getFluidTank).cast();
        
        return super.getCapability(cap, side);
    }
    
    public void setChanged() {
        super.setChanged();
        this.changed = true;
    }
}
