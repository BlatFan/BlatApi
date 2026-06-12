package ru.blatfan.blatapi.utils.gui_utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class EntityRotationState {
    public float yBodyRot = 0;
    public float yBodyRotO = 0;
    public float yRot = 0;
    public float yRotO = 0;
    public float xRot = 0;
    public float xRotO = 0;
    public float yHeadRot = 0;
    public float yHeadRotO = 0;
    
    public void set(Entity entity){
        entity.setYRot(yRot);
        entity.yRotO = yRotO;
        entity.setXRot(xRot);
        entity.xRotO = xRotO;
        
        if (entity instanceof LivingEntity living) {
            living.yBodyRot = yBodyRot;
            living.yBodyRotO = yBodyRotO;
            living.yHeadRot = yHeadRot;
            living.yHeadRotO = yHeadRotO;
        }
    }
}
