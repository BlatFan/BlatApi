package ru.blatfan.blatapi.compat.kubejs.blocked_stages;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.common.player_stages.blocked.BlockedStage;
import ru.blatfan.blatapi.common.player_stages.blocked.BlockedStagesManager;
import ru.blatfan.blatapi.common.task.Task;
import ru.blatfan.blatapi.utils.collection.Couple;
import ru.blatfan.blatapi.utils.collection.ItemStackList;

import java.util.List;

public class BlockedStagesEventJS extends EventJS {
    public BlockedStage<BlockedStage.ItemStageValue> createItemStage(ResourceLocation id, boolean visible, List<Task> tasks, List<ItemStack> list){
        BlockedStage.ItemStageValue value = new BlockedStage.ItemStageValue("item", ItemStackList.of(list));
        BlockedStage<BlockedStage.ItemStageValue> stage = new BlockedStage<>(tasks, visible, value);
        BlockedStagesManager.KUBEJS_ITEM_STAGES.put(id, stage);
        return stage;
    }
    
    public BlockedStage<BlockedStage.ItemStageValue> createLootStage(ResourceLocation id, boolean visible, List<Task> tasks, List<ItemStack> list){
        BlockedStage.ItemStageValue value = new BlockedStage.ItemStageValue("loot", ItemStackList.of(list));
        BlockedStage<BlockedStage.ItemStageValue> stage = new BlockedStage<>(tasks, visible, value);
        BlockedStagesManager.KUBEJS_LOOT_STAGES.put(id, stage);
        return stage;
    }
    
    public BlockedStage<BlockedStage.TagStageValue> createTagStage(ResourceLocation id, boolean visible, List<Task> tasks, List<Ingredient> list){
        BlockedStage.TagStageValue value = new BlockedStage.TagStageValue(list);
        BlockedStage<BlockedStage.TagStageValue> stage = new BlockedStage<>(tasks, visible, value);
        BlockedStagesManager.KUBEJS_TAG_STAGES.put(id, stage);
        return stage;
    }
    
    public BlockedStage<BlockedStage.ModStageValue> createModStage(ResourceLocation id, boolean visible, List<Task> tasks, String modid){
        BlockedStage.ModStageValue value = new BlockedStage.ModStageValue(modid);
        BlockedStage<BlockedStage.ModStageValue> stage = new BlockedStage<>(tasks, visible, value);
        BlockedStagesManager.KUBEJS_MOD_STAGES.put(id, stage);
        return stage;
    }
    
    public BlockedStage<BlockedStage.DimensionStageValue> createDimensionStage(ResourceLocation id, boolean visible, List<Task> tasks, ResourceLocation dim){
        BlockedStage.DimensionStageValue value = new BlockedStage.DimensionStageValue(dim);
        BlockedStage<BlockedStage.DimensionStageValue> stage = new BlockedStage<>(tasks, visible, value);
        BlockedStagesManager.KUBEJS_DIMENSION_STAGES.put(id, stage);
        return stage;
    }
    
    public BlockedStage<BlockedStage.BlockStageValue> createBlockStage(ResourceLocation id, boolean visible, List<Task> tasks, ResourceLocation block){
        return createBlockStage(id, visible, tasks, block, BlatApi.loc("empty"));
    }
    public BlockedStage<BlockedStage.BlockStageValue> createBlockStageWithReplacement(ResourceLocation id, List<Task> tasks, ResourceLocation block, ResourceLocation replacement){
        return createBlockStage(id, true, tasks, block, replacement);
    }
    
    @HideFromJS
    public BlockedStage<BlockedStage.BlockStageValue> createBlockStage(ResourceLocation id, boolean visible, List<Task> tasks, ResourceLocation block, ResourceLocation replacement){
        BlockedStage.BlockStageValue value = new BlockedStage.BlockStageValue(new Couple<>(block, replacement));
        BlockedStage<BlockedStage.BlockStageValue> stage = new BlockedStage<>(tasks, visible, value);
        BlockedStagesManager.KUBEJS_BLOCK_STAGES.put(id, stage);
        return stage;
    }
}