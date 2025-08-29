package ru.blatfan.blatapi.utils;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import ru.blatfan.blatapi.common.core.BlockPosDim;

public class PlayerUtil {

  public static void swapArmorStand(ArmorStand stand, Player player, InteractionHand hand) {
    ItemStack heldPlayer = player.getItemInHand(hand).copy();
    ItemStack heldStand = stand.getItemInHand(hand).copy();
    EquipmentSlot slot = (hand == InteractionHand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
    stand.setItemSlot(slot, heldPlayer);
    player.setItemSlot(slot, heldStand);
  }

  public static boolean isTamedByPlayer(AbstractHorse horse, Player dmgOwner) {
    return horse.isTamed() && horse.getOwnerUUID() != null &&
        horse.getOwnerUUID().equals(dmgOwner.getUUID());
  }

  public static boolean isTamedByPlayer(TamableAnimal horse, Player dmgOwner) {
    return horse.isTame() && horse.getOwnerUUID() != null &&
        horse.getOwnerUUID().equals(dmgOwner.getUUID());
  }

  public static void clearAllExp(Player player) {
    player.experienceProgress = 0;
    player.experienceLevel = 0;
    player.totalExperience = 0;
  }

  public static double getExpTotal(Player player) {
    int level = player.experienceLevel;
    double totalExp = getXpForLevel(level);
    double progress = Math.round(player.getXpNeededForNextLevel() * player.experienceProgress);
    totalExp += (int) progress;
    return totalExp;
  }

  public static boolean isPlayerCrouching(Entity entity) {
    return entity instanceof Player && ((Player) entity).isCrouching();
  }

  public static int getXpForLevel(int level) {
    int totalExp = 0;
    if (level <= 15) {
      totalExp = level * level + 6 * level;
    }
    else if (level <= 30) {
      totalExp = (int) (2.5 * level * level - 40.5 * level + 360);
    }
    else {
      totalExp = (int) (4.5 * level * level - 162.5 * level + 2220);
    }
    return totalExp;
  }

  public static ItemStack getPlayerItemIfHeld(Player player) {
    ItemStack wand = player.getMainHandItem();
    if (wand.isEmpty()) {
      wand = player.getOffhandItem();
    }
    return wand;
  }

  public static int getFirstSlotWithBlock(Player player, BlockState targetState) {
    int ret = -1;
    ItemStack stack;
    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
      stack = player.getInventory().getItem(i);
      if (!stack.isEmpty()) {
          stack.getItem();
          if (Block.byItem(stack.getItem()) == targetState.getBlock()) {
              return i;
          }
      }
    }
    return ret;
  }

  public static BlockState getBlockstateFromSlot(Player player, int slot) {
    ItemStack stack = player.getInventory().getItem(slot);
    if (!stack.isEmpty()) {
        stack.getItem();
        Block.byItem(stack.getItem());
        Block b = Block.byItem(stack.getItem());
        return b.defaultBlockState();
    }
    return null;
  }

  public static void decrStackSize(Player player, int slot) {
    if (!player.isCreative() && slot >= 0) {
      player.getInventory().removeItem(slot, 1);
    }
  }

  public static Item getItemArmorSlot(Player player, EquipmentSlot slot) {
    ItemStack inslot = player.getInventory().armor.get(slot.getIndex());
    Item item = (inslot.isEmpty()) ? null : inslot.getItem();
    return item;
  }

  public static Optional<Vec3> getPlayerHome(ServerPlayer player) {
    BlockPos respawnPos = player.getRespawnPosition();
    Optional<Vec3> optional = Optional.empty();
    if (respawnPos != null) {
      optional = Player.findRespawnPositionAndUseSpawnBlock((ServerLevel) player.level(), respawnPos, 0.0F, true, true);
    }
    return optional;
  }
  public static boolean hasFreeSlot(Player player){
    return getFreeSlot(player)>-1;
  }
  public static int getFreeSlot(Player player) {
    return player.getInventory().getFreeSlot();
  }
  public static void addItem(Player player, ItemStack stack){
    if (!player.getInventory().add(stack)) {
      Level level = player.level();
      ItemEntity entityItem = new ItemEntity(level, player.getX(), player.getY() + 0.5, player.getZ(), stack);
      entityItem.setPickUpDelay(0);
      level.addFreshEntity(entityItem);
    }
  }
  public static boolean hasItem(Player player, ItemStack stack){
    return itemCount(player, stack)>0;
  }
  public static int removeItem(Player player, ItemStack stack, int count){
    int remaining = count;
    
    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
      ItemStack slotStack = player.getInventory().getItem(i);
      
      if (ItemHelper.areStacksEqual(slotStack, stack)) {
        int toRemove = Math.min(slotStack.getCount(), remaining);
        slotStack.shrink(toRemove);
        remaining -= toRemove;
        
        if (remaining <= 0)
          break;
      }
    }
    
    return count - remaining;
  }
  public static int removeItem(Player player, ItemStack stack) {
    return removeItem(player, stack, 1);
  }
  public static int itemCount(Player player, ItemStack stack) {
    int count = 0;
    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
      ItemStack slotStack = player.getInventory().getItem(i);
      if (ItemHelper.areStacksEqual(slotStack, stack))
        count += slotStack.getCount();
    }
    return count;
  }
  public static int itemCount(Player player, Item item) {
    int count = 0;
    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
      ItemStack slotStack = player.getInventory().getItem(i);
      if (slotStack.is(item))
        count += slotStack.getCount();
    }
    return count;
  }
  public static boolean hasEnoughItems(Player player, ItemStack stack, int requiredCount) {
    return itemCount(player, stack) >= requiredCount;
  }
  public static NonNullList<ItemStack> copyInventory(Player player) {
    NonNullList<ItemStack> copy = NonNullList.withSize(player.getInventory().getContainerSize(), ItemStack.EMPTY);
    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
      ItemStack stack = player.getInventory().getItem(i);
      copy.set(i, stack.copy());
    }
    return copy;
  }
  public static int clearItem(Player player, ItemStack stack) {
    int removed = 0;
    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
      ItemStack slotStack = player.getInventory().getItem(i);
      if (ItemHelper.areStacksEqual(slotStack, stack)) {
        removed += slotStack.getCount();
        player.getInventory().setItem(i, ItemStack.EMPTY);
      }
    }
    return removed;
  }
  public static CompoundTag serializeInventory(Player player) {
    CompoundTag nbt = new CompoundTag();
    ListTag inventoryList = new ListTag();
    
    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
      ItemStack stack = player.getInventory().getItem(i);
      
      if (!stack.isEmpty()) {
        CompoundTag itemTag = new CompoundTag();
        itemTag.putByte("Slot", (byte) i);
        stack.save(itemTag);
        inventoryList.add(itemTag);
      }
    }
    
    nbt.put("Inventory", inventoryList);
    return nbt;
  }
  public static void deserializeInventory(Player player, CompoundTag nbt) {
    if (nbt.contains("Inventory", 9)) {
      ListTag inventoryList = nbt.getList("Inventory", 10);
      
      for (int i = 0; i < inventoryList.size(); i++) {
        CompoundTag itemTag = inventoryList.getCompound(i);
        int slot = itemTag.getByte("Slot") & 255;
        
        if (slot >= 0 && slot < player.getInventory().getContainerSize()) {
          ItemStack stack = ItemStack.of(itemTag);
          player.getInventory().setItem(slot, stack);
        }
      }
    }
  }
  public static void setCooldownItem(Player player, Item item, int cooldown) {
    player.getCooldowns().addCooldown(item, cooldown);
  }
  public static void dimensionTeleport(ServerPlayer player, ServerLevel world, BlockPosDim loc) {
    if (player instanceof FakePlayer) {
      return;
    }
    if (!player.canChangeDimensions()) {
      return;
    }
    if (!world.isClientSide) {
      DimensionTransit transit = new DimensionTransit(world, loc);
      transit.teleport(player);
      player.changeDimension(transit.getTargetLevel(), transit);
    }
  }
}