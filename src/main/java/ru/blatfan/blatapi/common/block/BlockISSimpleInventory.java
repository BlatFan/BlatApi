package ru.blatfan.blatapi.common.block;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;
import ru.blatfan.blatapi.utils.BaseItemStackHandler;

import javax.annotation.Nonnull;

@Getter
public abstract class BlockISSimpleInventory extends BlockEntity {
    private boolean isChanged = false;
    private final BaseItemStackHandler itemHandler = this.createItemHandler();
    
    protected abstract BaseItemStackHandler createItemHandler();
    
    public void drop(Level level, BlockPos pos){
        SimpleContainer container = new SimpleContainer(itemHandler.getSlots());
        for(int i=0; i<itemHandler.getSlots(); i++)
            container.addItem(itemHandler.getStackInSlot(i));
        Containers.dropContents(level, pos, container);
    }
    
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::saveWithFullMetadata);
    }
    
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
