package ru.blatfan.blatapi.compat.kubejs.util;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import ru.blatfan.blatapi.common.task.*;
import ru.blatfan.blatapi.common.task.stage.*;

import java.util.Arrays;
import java.util.List;

public class TaskBuilder {
    public static HasItemTask hasItem(boolean visible, int amount, ItemStack item) {
        return new HasItemTask(visible, amount, item);
    }
    
    public static ItemTask item(boolean visible, ItemStack item) {
        return new ItemTask(visible, item);
    }
    
    public static PositionTask position(boolean visible, int x, int y, int z, int distance) {
        return new PositionTask(new BlockPos(x,y,z), distance, visible);
    }
    
    public static AdvancementTask advancement(boolean visible, String advId) {
        return new AdvancementTask(ResourceLocation.parse(advId), visible);
    }
    
    public static CraftTask craft(boolean visible, ItemStack item) {
        return new CraftTask(visible, item);
    }
    
    public static KillTask kill(boolean visible, String entityId) {
        return new KillTask(visible, ResourceLocation.parse(entityId));
    }
    
    public static EatTask eat(boolean visible, ItemStack food) {
        return new EatTask(visible, food);
    }
    
    public static StageBoolTask stageBool(boolean visible, String stage) {
        return new StageBoolTask(visible, ResourceLocation.parse(stage));
    }
    
    public static StageIntTask stageInt(boolean visible, String stage, int min, int max) {
        return new StageIntTask(visible, ResourceLocation.parse(stage), min, max);
    }
    
    public static StageIntTask stageInt(boolean visible, String stage, int min) {
        return new StageIntTask(visible, ResourceLocation.parse(stage), min, Integer.MAX_VALUE);
    }
    
    public static StageDoubleTask stageDouble(boolean visible, String stage, double min, double max) {
        return new StageDoubleTask(visible, ResourceLocation.parse(stage), min, max);
    }
    
    public static StageDoubleTask stageDouble(boolean visible, String stage, double min) {
        return new StageDoubleTask(visible, ResourceLocation.parse(stage), min, Double.MAX_VALUE);
    }
    
    public static StageFloatTask stageFloat(boolean visible, String stage, float min, float max) {
        return new StageFloatTask(visible, ResourceLocation.parse(stage), min, max);
    }
    
    public static StageFloatTask stageFloat(boolean visible, String stage, float min) {
        return new StageFloatTask(visible, ResourceLocation.parse(stage), min, Float.MAX_VALUE);
    }
    
    public static StageLongTask stageLong(boolean visible, String stage, long min, long max) {
        return new StageLongTask(visible, ResourceLocation.parse(stage), min, max);
    }
    
    public static StageLongTask stageLong(boolean visible, String stage, long min) {
        return new StageLongTask(visible, ResourceLocation.parse(stage), min, Long.MAX_VALUE);
    }
    
    public static StageStringTask stageString(boolean visible, String stage, String value) {
        return new StageStringTask(visible, ResourceLocation.parse(stage), value);
    }
    
    public static StageItemStackTask stageItemStack(boolean visible, String stage, ItemStack value) {
        return new StageItemStackTask(visible, ResourceLocation.parse(stage), value);
    }
    
    public static NotTask not(boolean visible, Task task) {
        return new NotTask(visible, task);
    }
    
    public static List<Task> list(Task... tasks) {
        return Arrays.asList(tasks);
    }
}
