package ru.blatfan.blatapi.compat.jade;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.common.BARegistry;
import ru.blatfan.blatapi.common.task.Task;
import ru.blatfan.blatapi.utils.BlockedStageHelper;
import ru.blatfan.blatapi.utils.collection.Text;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.impl.ui.ItemStackElement;

public class LockedBlockProvider implements IBlockComponentProvider {
    @Override
    public ResourceLocation getUid() {
        return BlatApi.loc("locked_block");
    }
    
    @Override
    public @Nullable IElement getIcon(BlockAccessor accessor, IPluginConfig config, IElement currentIcon) {
        IElement icon = currentIcon;
        var player = accessor.getPlayer();
        var block = accessor.getBlock();
        
        if (!BlockedStageHelper.cannotInteractBlock(player, block)) return icon;
        
        var stage = BlockedStageHelper.getStageByBlock(block);
        if (stage == null) return icon;
        icon = ItemStackElement.of(new ItemStack(BARegistry.Items.QUESTION.get()));
        
        var value = stage.getValue();
        var pair = value.getBlockPair();
        ResourceLocation fakeId = pair.getValue();
        
        boolean hasFake = !fakeId.equals(BlatApi.loc("empty"));
        
        if (hasFake) {
            Block fakeBlock = BuiltInRegistries.BLOCK.get(fakeId);
            if (fakeBlock != Blocks.AIR) icon = ItemStackElement.of(new ItemStack(fakeBlock));
        }
        
        return icon;
    }
    
    @Override
    public boolean isRequired() {
        return true;
    }
    
    public int getDefaultPriority() {
        return -4501;
    }
    
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        var player = accessor.getPlayer();
        var block = accessor.getBlock();
        
        if (!BlockedStageHelper.cannotInteractBlock(player, block)) {
            return;
        }
        
        var stage = BlockedStageHelper.getStageByBlock(block);
        if (stage == null) {
            addTasks(tooltip, player, block);
            return;
        }
        
        var value = stage.getValue();
        var pair = value.getBlockPair();
        ResourceLocation fakeId = pair.getValue();
        
        boolean hasFake = !fakeId.equals(BlatApi.loc("empty"));
        
        if (!stage.isVisible() && !hasFake) {
            tooltip.clear();
            tooltip.add(Text.create(block.getName()).withStyle(ChatFormatting.OBFUSCATED));
            return;
        }
        
        if (hasFake) {
            tooltip.clear();
            Block fakeBlock = BuiltInRegistries.BLOCK.get(fakeId);
            if (fakeBlock != Blocks.AIR) {
                tooltip.add(Text.create(fakeBlock.getName()).withStyle(ChatFormatting.WHITE));
            } else {
                tooltip.add(Text.create(block.getName()).withStyle(ChatFormatting.OBFUSCATED));
                tooltip.add(Text.create());
                addTasks(tooltip, player, block);
            }
            return;
        }
        
        tooltip.add(Text.create());
        addTasks(tooltip, player, block);
    }
    
    private void addTasks(ITooltip tooltip, Player player, Block block) {
        for (Task task : BlockedStageHelper.getBlockStageTasks(player, block))
            tooltip.add(task.text(player));
    }
}