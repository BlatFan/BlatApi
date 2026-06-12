package ru.blatfan.blatapi.client.gui.screen;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.blatfan.blatapi.BlatApi;

public class ContainerMenuBase extends AbstractContainerMenu {
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    public Player playerEntity;
    public IItemHandler playerInventory;

    protected ContainerMenuBase(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    public int getInventorySize() {
        return 1;
    }

    public int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new InputSlot(handler, index, x, y));
            x += dx;
            index++;
        }

        return index;
    }

    public int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }

        return index;
    }

    public void layoutPlayerInventorySlots(int leftCol, int topRow) {
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }
    
    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = this.slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        
        if (index < 36 && !this.moveItemStackToSlotsWithRules(sourceStack, 36, 36 + this.getInventorySize(), false))
            return ItemStack.EMPTY;
        
        else {
            if (index >= 36 + this.getInventorySize()) {
                BlatApi.LOGGER.info("Invalid slotIndex: {}", index);
                return ItemStack.EMPTY;
            }
            
            if (!this.moveItemStackTo(sourceStack, 0, 36, true)) return ItemStack.EMPTY;
        }
        
        if (sourceStack.isEmpty()) sourceSlot.set(ItemStack.EMPTY);
        else sourceSlot.setChanged();
        
        sourceSlot.onTake(this.playerEntity, sourceStack);
        return copyOfSourceStack;
    }
    
    protected boolean moveItemStackToSlotsWithRules(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean success = false;
        int i = startIndex;
        if (reverseDirection) i = endIndex - 1;
        
        if (stack.isStackable()) {
            while (!stack.isEmpty() && (reverseDirection ? i >= startIndex : i < endIndex)) {
                Slot slot = this.slots.get(i);
                ItemStack slotStack = slot.getItem();
                
                if (!slotStack.isEmpty() && ItemStack.isSameItemSameTags(stack, slotStack) && slot.mayPlace(stack)) {
                    int combinedAmount = slotStack.getCount() + stack.getCount();
                    int maxLimit = Math.min(slot.getMaxStackSize(), stack.getMaxStackSize());
                    
                    if (combinedAmount <= maxLimit) {
                        stack.setCount(0);
                        slotStack.setCount(combinedAmount);
                        slot.setChanged();
                        success = true;
                    } else if (slotStack.getCount() < maxLimit) {
                        stack.shrink(maxLimit - slotStack.getCount());
                        slotStack.setCount(maxLimit);
                        slot.setChanged();
                        success = true;
                    }
                }
                
                if (reverseDirection) --i;
                else ++i;
            }
        }
        
        if (!stack.isEmpty()) {
            if (reverseDirection) i = endIndex - 1;
            else i = startIndex;
            
            while (reverseDirection ? i >= startIndex : i < endIndex) {
                Slot slot = this.slots.get(i);
                ItemStack slotStack = slot.getItem();
                
                if (slotStack.isEmpty() && slot.mayPlace(stack)) {
                    if (stack.getCount() > slot.getMaxStackSize()) slot.set(stack.split(slot.getMaxStackSize()));
                    else slot.set(stack.split(stack.getCount()));
                    
                    slot.setChanged();
                    success = true;
                    break;
                }
                
                if (reverseDirection) --i;
                else ++i;
            }
        }
        
        return success;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
