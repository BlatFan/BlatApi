package ru.blatfan.blatapi.fluffy_fur.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class FluffyFurClientConfig {
    public static ForgeConfigSpec.ConfigValue<Boolean>
            ITEM_PARTICLE, ITEM_GUI_PARTICLE, ITEM_IN_HAND_PARTICLE, BLOOD_PARTICLE, LIGHTNING_BOLT_EFFECT, EXPLOSION_EFFECT,
            MENU_BUTTON;
    public static ForgeConfigSpec.ConfigValue<Integer>
            MENU_BUTTON_ROW, MENU_BUTTON_ROW_X_OFFSET, MENU_BUTTON_X_OFFSET, MENU_BUTTON_Y_OFFSET;
    public static ForgeConfigSpec.ConfigValue<Double>
            SCREENSHAKE_INTENSITY;
    public static ForgeConfigSpec.ConfigValue<String>
            PANORAMA;

    public FluffyFurClientConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("Graphics").push("graphics");
        SCREENSHAKE_INTENSITY = builder.comment("Intensity of screenshake.").defineInRange("screenshakeIntensity", 1d, 0, 10d);

        builder.comment("Particles").push("particles");
        ITEM_PARTICLE = builder.comment("Enable dropping items particles.").define("itemParticle", true);
        ITEM_GUI_PARTICLE = builder.comment("Enable items particles in GUI.").define("itemGuiParticle", true);
        ITEM_IN_HAND_PARTICLE = builder.comment("Enable items GUI particles in hand.").define("itemParticleInHand", true);
        BLOOD_PARTICLE = builder.comment("Enable blood particles in case of damage.").define("bloodParticle", true);
        LIGHTNING_BOLT_EFFECT = builder.comment("Enable custom effect of lightning bolt.").define("lightningBoltEffect", true);
        EXPLOSION_EFFECT = builder.comment("Enable custom effect of explosion.").define("explosionEffect", true);
        builder.pop();
        builder.pop();

        builder.comment("Menu").push("menu");
        PANORAMA = builder.comment("Fluffy Fur Panorama.").define("panorama", "minecraft:vanilla");
        MENU_BUTTON = builder.comment("Enable Fluffy Fur menu button.").define("menuButton", true);
        MENU_BUTTON_ROW = builder.comment("Fluffy Fur menu button row.").defineInRange("menuButtonRow", 1, 0, 4);
        MENU_BUTTON_ROW_X_OFFSET = builder.comment("Fluffy Fur menu button X offset with row.").define("menuButtonRowXOffset", 4);
        MENU_BUTTON_X_OFFSET = builder.comment("Fluffy Fur menu button X offset.").define("menuButtonXOffset", 0);
        MENU_BUTTON_Y_OFFSET = builder.comment("Fluffy Fur menu button Y offset.").define("menuButtonYOffset", 0);
        builder.pop();
    }

    public static final FluffyFurClientConfig INSTANCE;
    public static final ForgeConfigSpec SPEC;

    static {
        final Pair<FluffyFurClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(FluffyFurClientConfig::new);
        SPEC = specPair.getRight();
        INSTANCE = specPair.getLeft();
    }
}
