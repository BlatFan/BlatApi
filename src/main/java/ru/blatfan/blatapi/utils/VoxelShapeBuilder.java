package ru.blatfan.blatapi.utils;

import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelShapeBuilder {
    protected VoxelShape shapes = Shapes.empty();
    
    protected VoxelShapeBuilder(){ init();}
    protected void init(){}
    
    public static VoxelShapeBuilder create(){
        return new VoxelShapeBuilder();
    }
    public static VoxelShapeBuilder create(VoxelShape shape){
        return new VoxelShapeBuilder(){
            @Override
            protected void init() {
                add(shape);
            }
        };
    }
    
    public VoxelShapeBuilder add(VoxelShape add){
        shapes = Shapes.or(shapes, add);
        return this;
    }
    
    public VoxelShape build(){
        return shapes;
    }
}