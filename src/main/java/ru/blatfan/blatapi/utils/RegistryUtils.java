package ru.blatfan.blatapi.utils;

import com.google.common.collect.ImmutableMap;
import lombok.experimental.UtilityClass;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import ru.blatfan.blatapi.common.fire.FireBlockHandler;
import ru.blatfan.blatapi.common.fire.FireBlockModifier;
import ru.blatfan.blatapi.client.particle.GenericParticle;
import ru.blatfan.blatapi.client.particle.ICustomParticleRender;
import ru.blatfan.blatapi.client.particle.behavior.ICustomBehaviorParticleRender;
import ru.blatfan.blatapi.client.render.LevelRenderHandler;

import java.util.ArrayList;
import java.util.Arrays;

@UtilityClass
public class RegistryUtils {
    public static void composterItem(float chance, ItemLike item) {
        ComposterBlock.add(chance, item);
    }
    
    public static void makeBow(Item item) {
        ItemProperties.register(item, ResourceLocation.parse("pull"), (stack, level, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
            }
        });
        
        ItemProperties.register(item, ResourceLocation.parse("pulling"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
    }
    public static FireBlock fireblock;
    
    public static Block[] getBlocks(Class<?>... blockClasses) {
        IForgeRegistry<Block> blocks = ForgeRegistries.BLOCKS;
        ArrayList<Block> matchingBlocks = new ArrayList<>();
        for (Block block : blocks) {
            if (Arrays.stream(blockClasses).anyMatch(b -> b.isInstance(block))) {
                matchingBlocks.add(block);
            }
        }
        return matchingBlocks.toArray(new Block[0]);
    }
    
    public static void setFireBlock() {
        fireblock = (FireBlock) Blocks.FIRE;
        FireBlockHandler.register(new FireBlockModifier());
    }
    
    public static void axeStrippables(Block block, Block result) {
        AxeItem.STRIPPABLES = new ImmutableMap.Builder<Block, Block>().putAll(AxeItem.STRIPPABLES).put(block, result).build();
    }
    
    public static void fireBlock(Block block, int encouragement, int flammability) {
        fireblock.setFlammable(block, encouragement, flammability);
    }
    
    
    public static void addParticleList(ICustomParticleRender particle) {
        LevelRenderHandler.particleList.add(particle);
    }
    
    public static void addBehaviorParticleList(GenericParticle particle, ICustomBehaviorParticleRender behavior) {
        LevelRenderHandler.behaviorParticleList.put(particle, behavior);
    }
}