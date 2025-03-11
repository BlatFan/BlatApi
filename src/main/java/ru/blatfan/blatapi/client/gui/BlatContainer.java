package ru.blatfan.blatapi.client.gui;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class BlatContainer extends AbstractContainerMenu {

  public static final int PLAYERSIZE = 4 * 9;
  protected Player playerEntity;
  protected Inventory playerInventory;
  protected int startInv = 0;
  protected int endInv = 17; //must be set by extending class

  protected BlatContainer(MenuType<?> type, int id) {
    super(type, id);
  }

  protected void trackEnergy(BlockEntity tile) {
    addDataSlot(new DataSlot() {

      @Override
      public int get() {
        return tile.getCapability(ForgeCapabilities.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
      }

      @Override
      public void set(int value) {
        //
      }
    });
  }

  @Override
  public ItemStack quickMoveStack(Player playerIn, int index) {
    try {
      //if last machine slot is 17, endInv is 18
      int playerStart = endInv;
      int playerEnd = endInv + PLAYERSIZE; //53 = 17 + 36  
      //standard logic based on start/end
      ItemStack itemstack = ItemStack.EMPTY;
      Slot slot = this.slots.get(index);
      if (slot != null && slot.hasItem()) {
        ItemStack stack = slot.getItem();
        itemstack = stack.copy();
        if (index < this.endInv) {
          if (!this.moveItemStackTo(stack, playerStart, playerEnd, false)) {
            return ItemStack.EMPTY;
          }
        }
        else if (index <= playerEnd && !this.moveItemStackTo(stack, startInv, endInv, false)) {
          return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
          slot.set(ItemStack.EMPTY);
        }
        else {
          slot.setChanged();
        }
        if (stack.getCount() == itemstack.getCount()) {
          return ItemStack.EMPTY;
        }
        slot.onTake(playerIn, stack);
      }
      return itemstack;
    }
    catch (Exception e) {
      return ItemStack.EMPTY;
    }
  }

  private int addSlotRange(Inventory handler, int index, int x, int y, int amount, int dx) {
    for (int i = 0; i < amount; i++) {
      addSlot(new Slot(handler, index, x, y));
      x += dx;
      index++;
    }
    return index;
  }

  private int addSlotBox(Inventory handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
    for (int j = 0; j < verAmount; j++) {
      index = addSlotRange(handler, index, x, y, horAmount, dx);
      y += dy;
    }
    return index;
  }

  protected void layoutPlayerInventorySlots(int leftCol, int topRow) {
    // Player inventory
    addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);
    // Hotbar
    topRow += 58;
    addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
  }

  public ItemStack findBag(Item item) {
    Player player = this.playerEntity;
    if (player.getMainHandItem().getItem() == item) {
      return player.getMainHandItem();
    }
    else if (player.getOffhandItem().getItem() == item) {
      return player.getOffhandItem();
    }
    else {
      for (int x = 0; x < playerInventory.getContainerSize(); x++) {
        ItemStack stack = playerInventory.getItem(x);
        if (stack.getItem() == item) {
          return stack;
        }
      }
    }
    return ItemStack.EMPTY;
  }
}
