package ru.blatfan.blatapi.utils;

import lombok.experimental.UtilityClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

@UtilityClass@SuppressWarnings("ALL")
public class HarvestUtil {

  public static IntegerProperty getAgeProp(BlockState blockState) {
    if (blockState.getBlock() instanceof CropBlock crop) {
      return CropBlock.AGE;
    }
    String age = CropBlock.AGE.getName();
    for (Property<?> p : blockState.getProperties()) {
      if (p != null && p.getName() != null
          && p.getName().equalsIgnoreCase(age)
          && p instanceof IntegerProperty ip) {
        return ip;
      }
    }
    return null;
  }

  public static boolean hasAgeProperty(BlockState bs) {
    return getAgeProp(bs) != null;
  }
  
  public static boolean growCrop(ItemStack stack, Level level, BlockPos blockPos) {
    if (BoneMealItem.growCrop(stack, level, blockPos)) {
      return true;
    } else {
      BlockState blockstate = level.getBlockState(blockPos);
      boolean flag = blockstate.isFaceSturdy(level, blockPos, Direction.UP);
      return flag && BoneMealItem.growWaterPlant(stack, level, blockPos.relative(Direction.UP), Direction.UP);
    }
  }
  
  public static boolean growCrop(Level level, BlockPos blockPos) {
    return growCrop(ItemStack.EMPTY, level, blockPos);
  }
}
