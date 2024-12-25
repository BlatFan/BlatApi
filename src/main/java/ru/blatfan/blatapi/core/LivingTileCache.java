package ru.blatfan.blatapi.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import ru.blatfan.blatapi.utils.LevelWorldUtil;

/**
 * Server side only
 */
public class LivingTileCache {

  private final Map<String, Set<BlockPos>> alive;
  private final String name;
  public static boolean debug = false;
  private static final Logger LOGGER = LogManager.getLogger();

  public LivingTileCache(String n) {
    name = n;
    alive = new HashMap<>();
  }

  private boolean add(String key, BlockPos pos) {
    if (!alive.containsKey(key)) {
      alive.put(key, new HashSet<>());
    }
    return alive.get(key).add(pos);
  }

  private boolean remove(String key, BlockPos pos) {
    if (alive.containsKey(key)) {
      return alive.get(key).remove(pos);
    }
    return false;
  }

  /**
   * Call from your tile entity public void setRemoved();
   * 
   * @param pos
   *          position
   * @param level
   *          for dimension key
   */
  public void unload(Level level, BlockPos pos) {
    if (!level.isClientSide) {
      final String key = LevelWorldUtil.dimensionToString(level);
      boolean removed = remove(key, pos);
      if (removed) {
        if (debug)
          LOGGER.info("[" + name + "] removed: " + key + pos + " size " + alive.get(key).size());
      }
    }
  }

  /**
   * Call from your tile entity public void onLoad();
   * 
   * @param pos
   *          position
   * @param level
   *          for dimension key
   */
  public void load(Level level, BlockPos pos) {
    if (!level.isClientSide) {
      final String key = LevelWorldUtil.dimensionToString(level);
      boolean added = add(key, pos);
      if (added) {
        if (debug)
          LOGGER.info("[" + name + "] added: " + key + pos + " size " + alive.get(key).size());
      }
      else {
        if (debug)
          LOGGER.info("[" + name + "] ping : " + key + pos + " size " + alive.get(key).size());
      }
    }
  }

  /**
   * True: at least one collision
   * 
   * False: no collisions in entire list
   */
  public boolean hasCollision(Level level, BlockPos mobPos, int radius, int height) {
    final String key = LevelWorldUtil.dimensionToString(level);
    if (alive.containsKey(key)) {
      for (BlockPos candle : alive.get(key)) {
        return LevelWorldUtil.withinArea(mobPos, radius, height, candle);
      }
    }
    return false;
  }

  public int size() {
    return alive.size();
  }
}
