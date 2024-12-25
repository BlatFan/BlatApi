package ru.blatfan.blatapi.core;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import ru.blatfan.blatapi.utils.LevelWorldUtil;

public class BlockPosDim {

  public static final BlockPosDim ORIGIN = new BlockPosDim(BlockPos.ZERO, "");
  private int x;
  private int y;
  private int z;
  private BlockPos pos;
  private String dimension;
  private String name;
  private Vec3 hitVec = Vec3.ZERO;
  private Direction side;
  private Direction sidePlayerFacing;

  public BlockPosDim(Entity entity) {
    this(entity.blockPosition(), entity.level());
  }

  public BlockPosDim(BlockPos pos, Level level) {
    this(pos, LevelWorldUtil.dimensionToString(level));
  }

  public BlockPosDim(BlockPos pos, String dimension) {
    this(pos, dimension, null);
  }

  public BlockPosDim(BlockPos pos, String dimension, CompoundTag stackTag) {
    setX(pos.getX());
    setY(pos.getY());
    setZ(pos.getZ());
    this.setPos(pos);
    this.setDimension(dimension);
    if (stackTag != null && stackTag.contains("display")) {
      //
      CompoundTag displayTag = stackTag.getCompound("display");
      if (displayTag != null && displayTag.contains("Name", 8)) {
        //
        Component namec = Component.Serializer.fromJson(displayTag.getString("Name"));
        this.name = namec.getString();
      }
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(dimension, pos, x, y, z);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    BlockPosDim other = (BlockPosDim) obj;
    return Objects.equals(dimension, other.dimension) && Objects.equals(pos, other.pos) && Double.doubleToLongBits(x) == Double.doubleToLongBits(other.x) && Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y) && Double.doubleToLongBits(z) == Double.doubleToLongBits(other.z);
  }

  @Override
  public String toString() {
    return getDisplayString();
  }

  public boolean isOrigin() {
    return this.equals(ORIGIN);
  }

  public BlockPos toBlockPos() {
    return new BlockPos(this.x, this.y, this.z);
  }

  public double getDistance(BlockPos pos) {
    double deltX = this.x - pos.getX();
    double deltY = this.y - pos.getY();
    double deltZ = this.z - pos.getZ();
    return Math.sqrt(deltX * deltX + deltY * deltY + deltZ * deltZ);
  }

  public String getDisplayString() {
    String prefix = "";
    if (name != null && !name.isEmpty()) {
      prefix = name + " ";
    }
    String d = dimension == null ? "" : dimension.replace("minecraft:", "");
    return prefix + d + " (" + (int) getX() + ", " + (int) getY() + ", " + (int) getZ() + ")";
  }

  public String getDimension() {
    return dimension;
  }

  public void setDimension(String dimension) {
    this.dimension = dimension;
  }

  public ServerLevel getTargetLevel(Level world) {
    if (world == null || world.getServer() == null || dimension == null) {
      return null;
    }
    return world.getServer().getLevel(LevelWorldUtil.stringToDimension(getDimension()));
  }

  public int getZ() {
    return z;
  }

  public void setZ(int z) {
    this.z = z;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public Direction getSide() {
    return side == null ? Direction.UP : side;
  }

  public void setSide(Direction side) {
    this.side = side;
  }

  public Vec3 getHitVec() {
    return hitVec;
  }

  public void setHitVec(Vec3 hitVec) {
    this.hitVec = hitVec;
  }

  public Direction getSidePlayerFacing() {
    return sidePlayerFacing;
  }

  public void setSidePlayerFacing(Direction sidePlayerFacing) {
    this.sidePlayerFacing = sidePlayerFacing;
  }

  public BlockPos getPos() {
    return pos;
  }

  public void setPos(BlockPos pos) {
    this.pos = pos;
  }
}
