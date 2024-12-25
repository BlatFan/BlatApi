package ru.blatfan.blatapi.utils;

import java.util.List;
import net.minecraft.core.BlockPos;

public class BlockPosUtil {

  /**
   * Sorts this list of positions based on distance to initial point
   * 
   * @param initPos
   * @param positions
   */
  public static void sortByDistance(final BlockPos initPos, List<BlockPos> positions) {
    positions.sort((pos0, pos1) -> {
      double dist0 = Math.sqrt(pos0.distSqr(initPos));
      double dist1 = Math.sqrt(pos1.distSqr(initPos));
      return Double.compare(dist0, dist1);
    });
  }
}
