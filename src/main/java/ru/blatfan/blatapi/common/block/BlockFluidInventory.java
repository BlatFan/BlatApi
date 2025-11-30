package ru.blatfan.blatapi.common.block;

import com.google.common.base.Preconditions;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;
import ru.blatfan.blatapi.utils.BlockUtil;

import javax.annotation.Nonnull;

@Getter
public abstract class BlockFluidInventory extends BlockEntity {
    private boolean changed = false;
    private final Container itemHandler = this.createItemHandler();
    private final FluidTank fluidTank = this.createFluidTank();
    
    public BlockFluidInventory(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }
    
    protected abstract Container createItemHandler();
    protected abstract FluidTank createFluidTank();
    
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::saveWithFullMetadata);
    }
    
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        CompoundTag tag = new CompoundTag();
        ContainerHelper.saveAllItems(tag, copyFromInv(itemHandler));
        pTag.put("inventory", tag);
        pTag.put("fluidTank", fluidTank.writeToNBT(new CompoundTag()));
        super.saveAdditional(pTag);
    }
    
    @Override
    public void load(CompoundTag pTag) {
        NonNullList<ItemStack> tmp = NonNullList.withSize(inventorySize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(pTag.getCompound("inventory"), tmp);
        copyToInv(tmp, itemHandler);
        fluidTank.readFromNBT(pTag.getCompound("fluidTank"));
        setChanged();
        super.load(pTag);
    }
    
    public final int inventorySize() {
        return getItemHandler().getContainerSize();
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return BlockUtil.getLazyItems(itemHandler).cast();
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return LazyOptional.of(this::getFluidTank).cast();
        return super.getCapability(cap, side);
    }
    
    @Override
    public void setChanged() {
        super.setChanged();
        this.changed = true;
    }
    
    protected static void copyToInv(NonNullList<ItemStack> src, Container dest) {
        Preconditions.checkArgument(src.size() == dest.getContainerSize());
        for (int i = 0; i < src.size(); i++) {
            dest.setItem(i, src.get(i));
        }
    }
    
    protected static NonNullList<ItemStack> copyFromInv(Container inv) {
        NonNullList<ItemStack> ret = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ret.set(i, inv.getItem(i));
        }
        return ret;
    }
}
