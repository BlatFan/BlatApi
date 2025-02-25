package ru.blatfan.blatapi.fluffy_fur.common.raycast;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class RayCast {

    public static RayHitResult getHit(Level level, RayCastContext context) {
        Vec3 start = context.getStartPos();
        Vec3 end = context.getEndPos();
        int entityCount = context.getEntityCount();
        float size = context.getEntitySize();
        boolean entityEnd = context.getEntityEnd();
        Predicate<BlockPos> blockPosFilter = context.getBlockPosFilter();
        Predicate<Entity> entityFilter = context.getEntityFilter();
        double distance = Math.sqrt(Math.pow(start.x() - end.x(), 2) + Math.pow(start.y() - end.y(), 2) + Math.pow(start.z() - end.z(), 2));
        double X = start.x();
        double Y = start.y();
        double Z = start.z();
        double oldX = X;
        double oldY = Y;
        double oldZ = Z;
        int count = 0;
        List<Entity> entities = new ArrayList<>();

        for (float i = 0; i < distance * 160; i++) {
            double dst = (distance * 160);

            double dX = start.x() - end.x();
            double dY = start.y() - end.y();
            double dZ = start.z() - end.z();

            double x = -(dX / (dst)) * i;
            double y = -(dY / (dst)) * i;
            double z = -(dZ / (dst)) * i;

            X = (start.x() + x);
            Y = (start.y() + y);
            Z = (start.z() + z);

            boolean canEntity = true;
            if (entityEnd) canEntity = (entityCount > 0);

            if (canEntity) {
                List<Entity> entityList = level.getEntitiesOfClass(Entity.class, new AABB(X - size, Y - size, Z - size, X + size, Y + size, Z + size));
                for (Entity entity : entityList) {
                    if (entityFilter.test(entity) && !entities.contains(entity)) {
                        entities.add(entity);
                        count++;

                        if (entityEnd && count >= entityCount) {
                            return new RayHitResult(new Vec3(X, Y, Z)).setEntities(entities);
                        }
                    }
                }
            }

            BlockPos blockPos = BlockPos.containing(X, Y, Z);
            BlockHitResult blockHitResult = context.getBlockShape(level.getBlockState(blockPos), level, blockPos).clip(start, end, blockPos);
            if (blockHitResult != null && blockPosFilter.test(blockPos)) {
                boolean isBlock = !level.getBlockState(blockHitResult.getBlockPos()).isAir();
                return new RayHitResult(new Vec3(oldX, oldY, oldZ)).setHitPos(new Vec3(X, Y, Z)).setBlockPos(blockHitResult.getBlockPos()).setDirection(blockHitResult.getDirection()).setBlock(isBlock).setEntities(entities);
            }
            blockHitResult = context.getFluidShape(level.getFluidState(blockPos), level, blockPos).clip(start, end, blockPos);
            if (blockHitResult != null) {
                return new RayHitResult(new Vec3(oldX, oldY, oldZ)).setHitPos(new Vec3(X, Y, Z)).setBlockPos(blockHitResult.getBlockPos()).setDirection(blockHitResult.getDirection()).setBlock(true).setEntities(entities);
            }

            oldX = X;
            oldY = Y;
            oldZ = Z;
        }
        return new RayHitResult(new Vec3(X, Y, Z)).setEntities(entities);
    }

    public static RayHitResult getHit(Level level, Vec3 start, Vec3 end) {
        return getHit(level, new RayCastContext(start, end));
    }

    public static List<Entity> getHitEntities(Level level, Vec3 start, Vec3 endPos, float distance) {
        List<Entity> list = new ArrayList<>();
        float ds = (float) Math.sqrt(Math.pow(start.x() - endPos.x(), 2) + Math.pow(start.y() - endPos.y(), 2) + Math.pow(start.z() - endPos.z(), 2));
        for (float i = 0; i < ds * 10; i++) {
            float dst = (ds * 10);

            double dX = start.x() - endPos.x();
            double dY = start.y() - endPos.y();
            double dZ = start.z() - endPos.z();

            float x = (float) -(dX / (dst)) * i;
            float y = (float) -(dY / (dst)) * i;
            float z = (float) -(dZ / (dst)) * i;

            float X = (float) (start.x() + x);
            float Y = (float) (start.y() + y);
            float Z = (float) (start.z() + z);

            List<Entity> entityList = level.getEntitiesOfClass(Entity.class, new AABB(X - distance, Y - distance, Z - distance, X + distance, Y + distance, Z + distance));
            for (Entity entity : entityList) {
                if (!list.contains(entity)) {
                    list.add(entity);
                }
            }
        }
        return list;
    }
}
