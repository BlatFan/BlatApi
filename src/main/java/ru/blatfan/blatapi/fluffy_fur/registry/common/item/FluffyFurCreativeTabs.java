package ru.blatfan.blatapi.fluffy_fur.registry.common.item;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

public class FluffyFurCreativeTabs {

    public static void addCreativeTabContent(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.OP_BLOCKS && event.hasPermissions()) {
            event.accept(FluffyFurItems.TEST_STICK);
        }
    }
}
