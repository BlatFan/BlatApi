package ru.blatfan.blatapi.utils.collection;

import net.minecraft.core.Vec3i;

public class Vector3Int {
    public int x;
    public int y;
    public int z;
    
    public Vector3Int() {}
    public Vector3Int(Vec3i vec) {
        this(vec.getX(), vec.getY(), vec.getZ());
    }
    public Vector3Int(int x, int y) {
        this(x, y, 0);
    }
    
    public Vector3Int(int d, int d1, int d2) {
        x = d;
        y = d1;
        z = d2;
    }
    
    public Vector3Int(Vector3Int vec) {
        x = vec.x;
        y = vec.y;
        z = vec.z;
    }
    
    public Vector3Int multiply(Vector3Int vec){
        x *= vec.x;
        y *= vec.y;
        z *= vec.z;
        return this;
    }
    
    public Vector3Int multiply(int d) {
        return multiply(new Vector3Int(d,d,d));
    }
    
    public Vector3Int copy() {
        return new Vector3Int(this);
    }
    
    public Vector3Int add(Vector3Int vec){
        x += vec.x;
        y += vec.y;
        z += vec.z;
        return this;
    }
    
    public Vector3Int subtract(Vector3Int vec) {
        x -= vec.x;
        y -= vec.y;
        z -= vec.z;
        return this;
    }
    
    public Vector3 toVector3(){
        return new Vector3(x,y,z);
    }
}
