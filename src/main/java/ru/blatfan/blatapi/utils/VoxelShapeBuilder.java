package ru.blatfan.blatapi.utils;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@SuppressWarnings("ALL")
public class VoxelShapeBuilder {
    protected VoxelShape shapes = Shapes.empty();
    
    protected VoxelShapeBuilder() {
        init();
    }
    protected void init() {}
    
    public static VoxelShapeBuilder create() {
        return new VoxelShapeBuilder();
    }
    
    public static VoxelShapeBuilder create(VoxelShape shape) {
        return new VoxelShapeBuilder() {
            @Override
            protected void init() {
                add(shape);
            }
        };
    }
    
    public VoxelShapeBuilder add(VoxelShape add) {
        shapes = Shapes.or(shapes, add);
        return this;
    }
    
    public VoxelShapeBuilder subtract(VoxelShape subtract) {
        shapes = Shapes.join(shapes, subtract, BooleanOp.ONLY_FIRST);
        return this;
    }
    
    public VoxelShapeBuilder box(double x1, double y1, double z1, double x2, double y2, double z2) {
        return add(Shapes.box(x1 / 16.0, y1 / 16.0, z1 / 16.0, x2 / 16.0, y2 / 16.0, z2 / 16.0));
    }
    
    public VoxelShapeBuilder boxSafe(double x1, double y1, double z1, double x2, double y2, double z2) {
        double minX = Math.min(x1, x2);
        double maxX = Math.max(x1, x2);
        double minY = Math.min(y1, y2);
        double maxY = Math.max(y1, y2);
        double minZ = Math.min(z1, z2);
        double maxZ = Math.max(z1, z2);
        return box(minX, minY, minZ, maxX, maxY, maxZ);
    }
    
    public VoxelShapeBuilder fullBlock() {
        return box(0, 0, 0, 16, 16, 16);
    }
    
    public VoxelShapeBuilder layer(double height) {
        return box(0, 0, 0, 16, height, 16);
    }
    
    public VoxelShapeBuilder addAll(VoxelShape... shapes) {
        for (VoxelShape shape : shapes) {
            add(shape);
        }
        return this;
    }
    
    public VoxelShapeBuilder rotate(Direction from, Direction to) {
        shapes = rotateShape(from, to, shapes);
        return this;
    }
    
    public VoxelShape build() {
        return shapes.optimize();
    }
    
    private static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        if (from == to) return shape;
        
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};
        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
        
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
                buffer[1] = Shapes.or(buffer[1],
                    Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX));
            });
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }
        
        return buffer[0];
    }
}