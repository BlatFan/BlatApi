package ru.blatfan.blatapi.compat.jade;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadeStagePlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new LockedBlockProvider(), Block.class);
        registration.registerEntityComponent(new LockedItemProvider(), ItemEntity.class);
    }
}
