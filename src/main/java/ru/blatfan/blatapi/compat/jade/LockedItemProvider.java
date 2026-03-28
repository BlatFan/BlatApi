package ru.blatfan.blatapi.compat.jade;

import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.common.task.Task;
import ru.blatfan.blatapi.utils.BlockedStageHelper;
import ru.blatfan.blatapi.utils.collection.Text;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.impl.ui.ItemStackElement;

public class LockedItemProvider implements IEntityComponentProvider {
    @Override
    public ResourceLocation getUid() {
        return BlatApi.loc("locked_item");
    }
    
    @Override
    public @Nullable IElement getIcon(EntityAccessor accessor, IPluginConfig config, IElement currentIcon) {
        return ItemStackElement.EMPTY;
    }
    
    @Override
    public boolean isRequired() {
        return true;
    }
    
    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        if (!(accessor.getEntity() instanceof ItemEntity itemEntity)) return;
        ItemStack stack = itemEntity.getItem();
        if (stack.isEmpty()) return;
        
        var player = accessor.getPlayer();
        if (BlockedStageHelper.canUse(player, stack)) return;
        
        var stage = BlockedStageHelper.getStageByItem(stack);
        
        if (stage != null && !stage.isVisible()) {
            tooltip.clear();
            tooltip.add(Text.create(stack.getHoverName()).withStyle(ChatFormatting.OBFUSCATED));
            tooltip.add(Text.create());
            for (Task task : BlockedStageHelper.getItemStageTasks(player, stack))
                tooltip.add(task.text(player));
            return;
        }
        
        tooltip.add(Text.create());
        for (Task task : BlockedStageHelper.getItemStageTasks(player, stack)) {
            tooltip.add(task.text(player));
        }
    }
}
