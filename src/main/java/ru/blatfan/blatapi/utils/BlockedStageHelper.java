package ru.blatfan.blatapi.utils;

import lombok.experimental.UtilityClass;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.ForgeRegistries;
import ru.blatfan.blatapi.common.BARegistry;
import ru.blatfan.blatapi.common.player_stages.blocked.BlockedStage;
import ru.blatfan.blatapi.common.player_stages.blocked.BlockedStage.*;
import ru.blatfan.blatapi.common.player_stages.blocked.BlockedStagesManager;
import ru.blatfan.blatapi.common.task.Task;
import ru.blatfan.blatapi.utils.collection.ItemStackList;
import ru.blatfan.blatapi.utils.collection.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@UtilityClass
public class BlockedStageHelper {
    private static BlockedStage<ItemStageValue> findItemStageForStack(ItemStack stack, String logicalType) {
        var stages = logicalType.equals("item")
            ? BlockedStagesManager.getItemStages()
            : BlockedStagesManager.getLootStages();
        
        for (BlockedStage<ItemStageValue> stage : stages) {
            ItemStageValue v = stage.getValue();
            if (!v.getLogicalType().equals(logicalType)) continue;
            if (has(v.getItems(), stack)) return stage;
        }
        return null;
    }
    
    private static BlockedStage<TagStageValue> findTagStage(ItemStack stack) {
        for (BlockedStage<TagStageValue> stage : BlockedStagesManager.getTagStages())
            for (Ingredient tag : stage.getValue().getTags())
                if (tag.test(stack)) return stage;
        return null;
    }
    private static BlockedStage<ModStageValue> findModStage(ItemStack stack) {
        String modId = ForgeHooks.getDefaultCreatorModId(stack);
        for (BlockedStage<ModStageValue> stage : BlockedStagesManager.getModStages())
            if (stage.getValue().getModId().equals(modId)) return stage;
        return null;
    }
    
    private static BlockedStage<BlockStageValue> findBlockStage(ResourceLocation blockId) {
        for (BlockedStage<BlockStageValue> stage : BlockedStagesManager.getBlockStages())
            if (stage.getValue().getBlockPair().getKey().equals(blockId))
                return stage;
        return null;
    }
    
    private static BlockedStage<DimensionStageValue> findDimensionStage(ResourceLocation dim) {
        for (BlockedStage<DimensionStageValue> stage : BlockedStagesManager.getDimensionStages())
            if (stage.getValue().getDimension().equals(dim)) return stage;
        return null;
    }
    
    
    public static BlockedStage<ItemStageValue> getItemStage(ItemStack stack) {
        if (stack.isEmpty()) return null;
        return findItemStageForStack(stack, "item");
    }
    
    public static BlockedStage<ItemStageValue> getLootStage(ItemStack stack) {
        if (stack.isEmpty()) return null;
        return findItemStageForStack(stack, "loot");
    }
    
    public static BlockedStage<BlockStageValue> getBlockStage(Block block) {
        return findBlockStage(BuiltInRegistries.BLOCK.getKey(block));
    }
    public static BlockedStage<BlockStageValue> getBlockStage(ResourceLocation blockId) {
        return findBlockStage(blockId);
    }
    public static BlockState getReplacedBlock(BlockState orig){
        BlockedStage<BlockStageValue> stage = getBlockStage(orig.getBlock());
        if(stage==null) return orig;
        Block b = BuiltInRegistries.BLOCK.get(stage.getValue().getBlockPair().getValue());
        return b==Blocks.AIR ? orig : b.defaultBlockState();
    }
    public static ResourceLocation getReplacedBlockID(BlockState orig){
        BlockedStage<BlockStageValue> stage = getBlockStage(orig.getBlock());
        if(stage==null) return null;
        return stage.getValue().getBlockPair().getValue();
    }
    public static BlockState getReplacedBlock(BlockState orig, Player player){
        return cannotInteractBlock(player, orig.getBlock()) ? getReplacedBlock(orig) : orig;
    }
    
    public static BlockedStage<BlockStageValue> getStageByBlock(Block block) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        return getBlockStage(id);
    }
    
    public static BlockedStage<DimensionStageValue> getDimensionStage(ResourceLocation dim) {
        return findDimensionStage(dim);
    }
    
    
    public static boolean has(ItemStackList list, ItemStack stack) {
        for (ItemStack itemStack : list) {
            if (ItemHelper.areStacksEqual(stack, itemStack)) return true;
        }
        return false;
    }
    
    public static BlockedStage<?> getStageByItem(ItemStack stack) {
        if (stack.isEmpty() || stack.is(BARegistry.Items.QUESTION.get())) return null;
        
        List<BlockedStage<?>> stages = findAllStagesForItem(stack);
        return stages.isEmpty() ? null : stages.get(0);
    }
    
    public static List<BlockedStage<?>> findAllStagesForItem(ItemStack stack) {
        List<BlockedStage<?>> stages = new ArrayList<>();
        
        BlockedStage<ItemStageValue> itemStage = getItemStage(stack);
        if (itemStage != null) stages.add(itemStage);
        
        BlockedStage<TagStageValue> tagStage = findTagStage(stack);
        if (tagStage != null) stages.add(tagStage);
        
        BlockedStage<ModStageValue> modStage = findModStage(stack);
        if (modStage != null) stages.add(modStage);
        
        Block block = Block.byItem(stack.getItem());
        if (block != Blocks.AIR) {
            BlockedStage<BlockStageValue> blockStage = getStageByBlock(block);
            if (blockStage != null) stages.add(blockStage);
        }
        
        return stages;
    }
    
    
    public static boolean canUse(Player player, ItemStack stack) {
        if (player == null) return false;
        if (player.isCreative()) return true;
        if (stack.isEmpty() || stack.is(BARegistry.Items.QUESTION.get())) return true;
        
        Block block = Block.byItem(stack.getItem());
        if (cannotInteractBlock(player, block)) return false;
        
        List<BlockedStage<?>> stages = findAllStagesForItem(stack);
        for (BlockedStage<?> stage : stages)
            if (!Task.get(player, stage.getTasks()))
                return false;
        
        return true;
    }
    
    public static boolean isVisible(Player player, ItemStack stack) {
        if (player == null) return true;
        if (player.isCreative()) return true;
        if (stack.isEmpty()) return false;
        
        List<BlockedStage<?>> stages = findAllStagesForItem(stack);
        for (BlockedStage<?> stage : stages)
            if (!stage.isVisible())
                return false;
        
        return true;
    }
    
    public static List<Task> getItemStageTasks(Player player, ItemStack stack) {
        if (player == null || player.isCreative()) return List.of();
        
        List<Task> allTasks = new ArrayList<>();
        List<BlockedStage<?>> stages = findAllStagesForItem(stack);
        
        for (BlockedStage<?> stage : stages)
            for (Task task : stage.getTasks())
                if (!task.get(player))
                    allTasks.add(task);
                    
        return allTasks;
    }
    
    public static boolean canAccessDimension(Player player, ResourceLocation dimension) {
        if (player == null) return false;
        if (player.isCreative()) return true;
        
        BlockedStage<DimensionStageValue> stage = getDimensionStage(dimension);
        if (stage == null) return true;
        
        return Task.get(player, stage.getTasks());
    }
    
    public static Text getLastDimensionTask(Player player, ResourceLocation dimension) {
        if (player == null || player.isCreative()) return Text.create();
        BlockedStage<DimensionStageValue> stage = getDimensionStage(dimension);
        if (stage == null) return Text.create();
        
        List<Task> tasks = new ArrayList<>();
        for (Task task : stage.getTasks()) {
            if (!task.get(player)) tasks.add(task);
        }
        if (tasks.isEmpty()) return Text.create();
        return tasks.get(0).text(player);
    }
    
    public static boolean canGetLoot(Player player, ItemStack item) {
        if (player == null) return true;
        if (player.isCreative()) return true;
        if (!canUse(player, item)) return false;
        
        BlockedStage<ItemStageValue> stage = getLootStage(item);
        if (stage == null) return true;
        
        return Task.get(player, stage.getTasks());
    }
    
    public static boolean cannotInteractBlock(Player player, Block block) {
        if (player == null) return true;
        if (player.isCreative()) return false;
        if (block == null) return false;
        
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        BlockedStage<BlockStageValue> stage = getBlockStage(id);
        if (stage == null) return false;
        
        return !Task.get(player, stage.getTasks());
    }
    
    public static List<Task> getBlockStageTasks(Player player, Block block) {
        if (player == null || player.isCreative() || block == null) return List.of();
        
        BlockedStage<BlockStageValue> stage = getStageByBlock(block);
        List<Task> tasks = new ArrayList<>();
        if (stage != null) {
            for (Task task : stage.getTasks()) {
                if (!task.get(player)) tasks.add(task);
            }
        }
        return tasks;
    }
    
    public static List<ItemStack> getBlockedItems() {
        List<ItemStack> items = new ArrayList<>();
        
        for (BlockedStage<ItemStageValue> stage : BlockedStagesManager.getItemStages()) items.addAll(stage.getValue().getItems());
        
        for (BlockedStage<BlockStageValue> stage : BlockedStagesManager.getBlockStages()) {
            ResourceLocation blockId = stage.getValue().getBlockPair().getKey();
            Block block = ForgeRegistries.BLOCKS.getValue(blockId);
            if (block != null && block != Blocks.AIR && block.asItem() != Items.AIR) items.add(new ItemStack(block));
        }
        
        for (BlockedStage<TagStageValue> stage : BlockedStagesManager.getTagStages())
            for (Ingredient ing : stage.getValue().getTags()) items.addAll(List.of(ing.getItems()));
        
        for (BlockedStage<ModStageValue> stage : BlockedStagesManager.getModStages()) {
            String modId = stage.getValue().getModId();
            for (Item item : findItemFromMod(modId))
                if (item!=null && item != Items.AIR) items.add(new ItemStack(item));
        }
        
        return items.stream().distinct().toList();
    }
    
    private static List<Item> findItemFromMod(String modId) {
        return ForgeRegistries.ITEMS.getEntries().stream()
            .filter(entry -> modId.equals(ForgeHooks.getDefaultCreatorModId(entry.getValue().getDefaultInstance())))
            .map(Map.Entry::getValue).toList();
    }
}