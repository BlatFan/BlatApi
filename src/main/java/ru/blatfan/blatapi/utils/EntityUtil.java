package ru.blatfan.blatapi.utils;

import java.util.ArrayList;
import java.util.List;

import lombok.experimental.UtilityClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import ru.blatfan.blatapi.utils.collection.Vector3;

@UtilityClass@SuppressWarnings("ALL")
public class EntityUtil {

  private static final double ENTITY_PULL_DIST = 0.4;
  private static final double ENTITY_PULL_SPEED_CUTOFF = 3;
  private static final float ITEMSPEEDFAR = 0.9F;
  private static final float ITEMSPEEDCLOSE = 0.2F;
  private static final int TICKS_FALLDIST_SYNC = 22;

  public static boolean haveSameDimension(Entity tamed, Entity owner) {
    return LevelWorldUtil.dimensionToString(tamed.level()).equalsIgnoreCase(
        LevelWorldUtil.dimensionToString(owner.level()));
  }

  public static boolean isTamedByPlayer(Entity entity, Player player) {
    if (entity instanceof OwnableEntity tamed) {
      return tamed.getOwnerUUID() != null &&
          tamed.getOwnerUUID().equals(player.getUUID());
    }
    else if (entity instanceof AbstractHorse) {
      AbstractHorse horse = (AbstractHorse) entity;
      return horse.getOwnerUUID() != null &&
          horse.getOwnerUUID().equals(player.getUUID());
    }
    return false;
  }

  private static boolean enderTeleportEvent(LivingEntity player, Level world, double x, double y, double z) {
    if (player.getRootVehicle() != player) {
      return false;
    }
    
    EntityUtil.teleportWallSafe(player, world, x, y, z);
    return true;
  }

  public static boolean enderTeleportEvent(LivingEntity player, Level world, BlockPos target) {
    return enderTeleportEvent(player, world, target.getX() + .5F, target.getY() + .5F, target.getZ() + .5F);
  }

  private static void teleportWallSafe(LivingEntity player, Level world, double x, double y, double z) {
    BlockPos coords = BlockPos.containing(x, y, z);
    world.getChunk(coords).setUnsaved(true);
    player.teleportTo(x, y, z);
    moveEntityWallSafe(player, world);
  }

  public static void moveEntityWallSafe(Entity entity, Level world) {
    while (world.noCollision(entity) == false) {
      entity.teleportTo(entity.xo, entity.yo + 1.0D, entity.zo);
    }
  }

  public static Direction getFacing(LivingEntity entity) {
    int yaw = (int) entity.getYRot();
    if (yaw < 0)
      yaw += 360;
    
    yaw += 22;
    yaw %= 360;
    
    int facing = yaw / 45;
    return Direction.from2DDataValue(facing / 2);
  }

  public static double getSpeedTranslated(double speed) {
    return speed * 100;
  }

  /**
   * Launch entity in the fixed facing direction given
   *
   * @param entity
   * @param power
   * @param facing
   */
  public static void launchDirection(Entity entity, float power, Direction facing) {
    double velX = 0;
    double velZ = 0;
    double velY = 0;
    switch (facing) {
      case EAST:
        velX = Math.abs(power);
        velZ = 0;
      break;
      case WEST:
        velX = -1 * Math.abs(power);
        velZ = 0;
      break;
      case NORTH:
        velX = 0;
        velZ = -1 * Math.abs(power);
      break;
      case SOUTH:
        velX = 0;
        velZ = Math.abs(power);
      break;
      case UP:
      case DOWN:
      default:
      break;
    }
    Entity ridingEntity = entity.getVehicle();
    if (ridingEntity != null) {
      entity.setDeltaMovement(entity.getDeltaMovement().x, 0, entity.getDeltaMovement().z);
      ridingEntity.fallDistance = 0;
      ridingEntity.push(velX, velY, velZ);
    } else {
      entity.setDeltaMovement(entity.getDeltaMovement().x, 0, entity.getDeltaMovement().z);
      entity.fallDistance = 0;
      entity.push(velX, velY, velZ);
    }
  }

  /**
   * Launch entity in the direction it is already facing
   *
   * @param entity
   * @param rotationPitch
   * @param power
   */
  public static void launch(Entity entity, float rotationPitch, float power) {
    float rotationYaw = entity.getYRot();
    launch(entity, rotationPitch, rotationYaw, power);
  }

  public static void launch(Entity entity, float rotationPitch, float rotationYaw, float power) {
    float mountPower = (float) (power + 0.5);
    double velX = -Mth.sin(rotationYaw / 180.0F * (float) Math.PI) * Mth.cos(rotationPitch / 180.0F * (float) Math.PI) * power;
    double velZ = Mth.cos(rotationYaw / 180.0F * (float) Math.PI) * Mth.cos(rotationPitch / 180.0F * (float) Math.PI) * power;
    double velY = -Mth.sin((rotationPitch) / 180.0F * (float) Math.PI) * power;
    if (velY < 0)
      velY *= -1;
    Entity ridingEntity = entity.getVehicle();
    if (ridingEntity != null) {
      entity.setDeltaMovement(entity.getDeltaMovement().x, 0, entity.getDeltaMovement().z);
      ridingEntity.fallDistance = 0;
      ridingEntity.push(velX * mountPower, velY * mountPower, velZ * mountPower);
    }
    else {
      entity.setDeltaMovement(entity.getDeltaMovement().x, 0, entity.getDeltaMovement().z);
      entity.fallDistance = 0;
      entity.push(velX, velY, velZ);
    }
  }

  public static AABB makeBoundingBox(BlockPos center, int hRadius, int vRadius) {
    return makeBoundingBox(center.getX(), center.getY(), center.getZ(), hRadius, vRadius);
  }

  public static AABB makeBoundingBox(double x, double y, double z, int hRadius, int vRadius) {
    return new AABB(
        x - hRadius, y - vRadius, z - hRadius,
        x + hRadius, y + vRadius, z + hRadius);
  }

  public static int moveEntityItemsInRegion(Level world, BlockPos pos, int hRadius, int vRadius) {
    return moveEntityItemsInRegion(world, pos.getX(), pos.getY(), pos.getZ(), hRadius, vRadius, true);
  }

  public static int moveEntityItemsInRegion(Level world, double x, double y, double z, int hRadius, int vRadius, boolean towardsPos) {
    AABB range = makeBoundingBox(x, y, z, hRadius, vRadius);
    List<Entity> all = getItemExp(world, range);
    return pullEntityList(x, y, z, towardsPos, all);
  }

  public static List<Entity> getItemExp(Level world, AABB range) {
    List<Entity> all = new ArrayList<Entity>();
    all.addAll(world.getEntitiesOfClass(ItemEntity.class, range));
    all.addAll(world.getEntitiesOfClass(ExperienceOrb.class, range));
    return all;
  }

  public static boolean speedupEntityIfMoving(LivingEntity entity, float factor) {
    if (entity.zza > 0) {
      if (entity.getVehicle() != null && entity.getVehicle() instanceof LivingEntity) {
        speedupEntity((LivingEntity) entity.getVehicle(), factor);
        return true;
      }
      else {
        speedupEntity(entity, factor);
        return true;
      }
    }
    return false;
  }

  public static void speedupEntity(LivingEntity entity, float factor) {
    float x = Mth.sin(-entity.getYRot() * 0.017453292F) * factor;
    float z = Mth.cos(entity.getYRot() * 0.017453292F) * factor;
    entity.setDeltaMovement(x, entity.getDeltaMovement().y, z);
  }

  public static int pullEntityList(double x, double y, double z, boolean towardsPos, List<? extends Entity> all) {
    return pullEntityList(x, y, z, towardsPos, all, ITEMSPEEDCLOSE, ITEMSPEEDFAR);
  }

  public static int pullEntityList(double x, double y, double z, boolean towardsPos, List<? extends Entity> all, float speedClose, float speedFar) {
    int moved = 0;
    double hdist, xDist, zDist;
    float speed;
    int direction = (towardsPos) ? 1 : -1;
    for (Entity entity : all) {
      if (entity == null)
        continue;
      if (entity instanceof Player && entity.isCrouching())
        continue;
      
      BlockPos p = entity.blockPosition();
      xDist = Math.abs(x - p.getX());
      zDist = Math.abs(z - p.getZ());
      hdist = Math.sqrt(xDist * xDist + zDist * zDist);
      if (hdist > ENTITY_PULL_DIST) {
        speed = (hdist > ENTITY_PULL_SPEED_CUTOFF) ? speedFar : speedClose;
        setEntityMotionFromVector(entity, x, y, z, direction * speed);
        moved++;
      }
    }
    return moved;
  }

  public static void setEntityMotionFromVector(Entity entity, double x, double y, double z, float modifier) {
    Vector3 originalPosVector = new Vector3(x, y, z);
    Vector3 entityVector = new Vector3(entity);
    Vector3 finalVector = originalPosVector.copy().subtract(entityVector);
    if (finalVector.mag() > 1) {
      finalVector.normalize();
    }
    double motionX = finalVector.x * modifier;
    double motionY = finalVector.y * modifier;
    double motionZ = finalVector.z * modifier;
    entity.setDeltaMovement(motionX, motionY, motionZ);
  }

  public static void addOrMergePotionEffect(LivingEntity entity, MobEffectInstance newp) {
    if (entity.hasEffect(newp.getEffect())) {
      MobEffectInstance p = entity.getEffect(newp.getEffect());
      int ampMax = Math.max(p.getAmplifier(), newp.getAmplifier());
      int dur = newp.getDuration() + p.getDuration();
      entity.addEffect(new MobEffectInstance(newp.getEffect(), dur, ampMax));
    }
    else {
      entity.addEffect(newp);
    }
  }

  /**
   * Force horizontal centering, so move from 2.9, 6.2 => 2.5,6.5
   *
   * @param entity
   * @param pos
   */
  public static void centerEntityHoriz(Entity entity, BlockPos pos) {
    float fixedX = pos.getX() + 0.5F;
    float fixedZ = pos.getZ() + 0.5F;
    entity.setPos(fixedX, entity.blockPosition().getY(), fixedZ);
  }

  public static List<Villager> getVillagers(Level world, BlockPos p, int r) {
    BlockPos start = p.offset(-r, -r, -r);
    BlockPos end = p.offset(r, r, r);
    return world.getEntitiesOfClass(Villager.class, new AABB(start, end));
  }

  public static LivingEntity getClosestEntity(Level world, Player player, List<? extends LivingEntity> list) {
    LivingEntity closest = null;
    double minDist = 999999;
    double dist, xDistance, zDistance;
    for (LivingEntity ent : list) {
      xDistance = Math.abs(player.xo - ent.xo);
      zDistance = Math.abs(player.zo - ent.zo);
      dist = Math.sqrt(xDistance * xDistance + zDistance * zDistance);
      if (dist < minDist) {
        minDist = dist;
        closest = ent;
      }
    }
    return closest;
  }

  public static Villager getVillager(Level world, int x, int y, int z) {
    List<Villager> all = world.getEntitiesOfClass(Villager.class, new AABB(new BlockPos(x, y, z)));
    if (all.size() == 0) {
      return null;
    }
    else {
      return all.get(0);
    }
  }

  public static float yawDegreesBetweenPoints(double posX, double posY, double posZ, double posX2, double posY2, double posZ2) {
    float f = (float) ((180.0f * Math.atan2(posX2 - posX, posZ2 - posZ)) / (float) Math.PI);
    return f;
  }

  public static float pitchDegreesBetweenPoints(double posX, double posY, double posZ, double posX2, double posY2, double posZ2) {
    return (float) Math.toDegrees(Math.atan2(posY2 - posY, Math.sqrt((posX2 - posX) * (posX2 - posX) + (posZ2 - posZ) * (posZ2 - posZ))));
  }

  public static Vec3 lookVector(float rotYaw, float rotPitch) {
    return new Vec3(
        Math.sin(rotYaw) * Math.cos(rotPitch),
        Math.sin(rotPitch),
        Math.cos(rotYaw) * Math.cos(rotPitch));
  }

  public static float getYawFromFacing(Direction currentFacing) {
    switch (currentFacing) {
      case DOWN:
      case UP:
      case SOUTH:
      default:
        return 0;
      case EAST:
        return 270F;
      case NORTH:
        return 180F;
      case WEST:
        return 90F;
    }
  }

  public static void setEntityFacing(LivingEntity entity, Direction currentFacing) {
    float yaw = 0;
    switch (currentFacing) {
      case EAST:
        yaw = 270F;
      break;
      case NORTH:
        yaw = 180F;
      break;
      case WEST:
        yaw = 90F;
      break;
      case DOWN:
      case UP:
      case SOUTH:
      default:
        yaw = 0;
    }
    entity.setYRot(yaw);
  }

  /**
   * used by bounce potion and vector plate
   *
   * @param entity
   * @param verticalMomentumFactor
   */
  public static void dragEntityMomentum(LivingEntity entity, double verticalMomentumFactor) {
    double x = entity.getDeltaMovement().x / verticalMomentumFactor;
    double z = entity.getDeltaMovement().z / verticalMomentumFactor;
    entity.setDeltaMovement(x, entity.getDeltaMovement().y, z);
  }

  public static Attribute getAttributeJump(Horse ahorse) {
    return Attributes.JUMP_STRENGTH; //was reflection lol
  }

  public static void eatingHorse(Horse ahorse) {
    ahorse.eating(); // requires accesstransformer.cfg 
  }
}
