package ru.blatfan.blatapi.utils.collection;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class Vector3 {

  public double x;
  public double y;
  public double z;

  public Vector3() {}
  public Vector3(BlockPos pos) { this(pos.getCenter());}
  public Vector3(Vec3 vec) {
    this(vec.x, vec.y, vec.z);
  }
  public Vector3(double x, double y) {
    this(x, y, 0);
  }

  public Vector3(double d, double d1, double d2) {
    x = d;
    y = d1;
    z = d2;
  }

  public Vector3(Entity e) {
    this(e.xOld, e.yOld, e.zOld);
  }

  public double mag() {
    return Math.sqrt(x * x + y * y + z * z);
  }

  public Vector3 normalize() {
    double d = mag();
    if (d != 0) {
      multiply(1 / d);
    }
    return this;
  }

  public Vector3(Vector3 vec) {
    x = vec.x;
    y = vec.y;
    z = vec.z;
  }

  public Vector3 multiply(Vector3 vec){
    x *= vec.x;
    y *= vec.y;
    z *= vec.z;
    return this;
  }
  
  public Vector3 multiply(double d) {
    return multiply(new Vector3(d,d,d));
  }

  public Vector3 copy() {
    return new Vector3(this);
  }

  public Vector3 add(Vector3 vec){
    x += vec.x;
    y += vec.y;
    z += vec.z;
    return this;
  }
  
  public Vector3 subtract(Vector3 vec) {
    x -= vec.x;
    y -= vec.y;
    z -= vec.z;
    return this;
  }
}
