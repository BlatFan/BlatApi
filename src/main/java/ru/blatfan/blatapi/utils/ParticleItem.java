package ru.blatfan.blatapi.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import ru.blatfan.blatapi.fluffy_fur.common.item.IGuiParticleItem;
import ru.blatfan.blatapi.fluffy_fur.common.item.IParticleItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

public interface ParticleItem extends IGuiParticleItem, IParticleItem {
    Color getColor();
    
    @OnlyIn(Dist.CLIENT)
    @Override
    default void renderParticle(PoseStack pose, LivingEntity livingEntity, Level level, ItemStack itemStack, int x, int y, int seed, int guiOffset) {
        guiParticle(pose, x, y, 100, seed);
    }
    
    @OnlyIn(Dist.CLIENT)
    void guiParticle(PoseStack pose, double x, double y, double z, int seed);
    @OnlyIn(Dist.CLIENT)
    default void guiParticle(PoseStack pose, int seed){
        guiParticle(pose, 0, 0, 0, seed);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    default void addParticles(Level level, ItemEntity itemEntity) {
        worldParticles(itemEntity.getX(), itemEntity.getY(), itemEntity.getZ());
    }
    
    @OnlyIn(Dist.CLIENT)
    void worldParticles(double x, double y, double z);
    
    default boolean renderInHand(){
        return true;
    }
}
