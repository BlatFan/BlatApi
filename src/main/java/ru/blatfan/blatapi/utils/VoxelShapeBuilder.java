package ru.blatfan.blatapi.utils;

import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelShapeBuilder {
    protected VoxelShape shape = Shapes.empty();
    
    protected VoxelShapeBuilder(){ init();}
    protected void init(){}
    
    public static VoxelShapeBuilder create(){
        return new VoxelShapeBuilder();
    }
    public static VoxelShapeBuilder create(VoxelShape shape1){
        return new VoxelShapeBuilder(){
            @Override
            protected void init() {
                this.shape=shape1;
            }
        };
    }
    
    protected VoxelShapeBuilder action(VoxelShape add, BooleanOp function){
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