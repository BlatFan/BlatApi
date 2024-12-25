package ru.blatfan.blatapi.fluffy_fur.registry.common.block;

import com.google.common.collect.ImmutableMap;
import ru.blatfan.blatapi.fluffy_fur.FluffyFur;
import ru.blatfan.blatapi.fluffy_fur.common.fire.FireBlockHandler;
import ru.blatfan.blatapi.fluffy_fur.common.fire.FireBlockModifier;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.Arrays;

public class FluffyFurBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FluffyFur.MOD_ID);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
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
}
