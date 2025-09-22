package ru.blatfan.blatapi.mixins.common;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;
import ru.blatfan.blatapi.common.task.EatTask;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    @Override
    public ItemStack eat(Level pLevel, ItemStack pFood) {
        if(pFood.isEdible() && PlayerStages.allStages.contains(EatTask.getStage(pFood.getItem())))
            PlayerStages.add((Player) ((Object) this), EatTask.getStage(pFood.getItem()));
        return super.eat(pLevel, pFood);
    }
}