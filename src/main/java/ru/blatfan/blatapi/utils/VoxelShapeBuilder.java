package ru.blatfan.blatapi.utils;

import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelShapeBuilder {
    private VoxelShape shape;
    
    protected VoxelShapeBuilder(){}
    
    public static VoxelShapeBuilder create(){
        return new VoxelShapeBuilder();
    }
    
    private VoxelShapeBuilder action(VoxelShape add, BooleanOp function){
        shape = Shapes.join(shape, add, function);
        return this;
    }
    
    public VoxelShapeBuilder add(VoxelShape add){
        return action(add, BooleanOp.AND);
    }
    
    public VoxelShapeBuilder or(VoxelShape add){
        return action(add, BooleanOp.OR);
    }
    
    public VoxelShape build(){
        return shape;
    }
}