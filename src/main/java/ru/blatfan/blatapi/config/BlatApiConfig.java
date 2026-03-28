package ru.blatfan.blatapi.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class BlatApiConfig {
    public static ForgeConfigSpec.ConfigValue<Boolean> ITEM_PARTICLE;
    public static ForgeConfigSpec.ConfigValue<Integer> MENU_BUTTON_ROW;

    public BlatApiConfig(ForgeConfigSpec.Builder builder) {}

    public static final BlatApiConfig INSTANCE;
    public static final ForgeConfigSpec SPEC;

    static {
        final Pair<BlatApiConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(BlatApiConfig::new);
        SPEC = specPair.getRight();
        INSTANCE = specPair.getLeft();
    }
}
