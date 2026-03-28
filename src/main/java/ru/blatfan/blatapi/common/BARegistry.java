package ru.blatfan.blatapi.common;

import com.mojang.serialization.Codec;
import net.minecraft.client.gui.Font;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.FontHandler;
import ru.blatfan.blatapi.client.particle.type.*;
import ru.blatfan.blatapi.common.block.sign.*;
import ru.blatfan.blatapi.common.creativetab.MultiCreativeTab;
import ru.blatfan.blatapi.common.creativetab.SubCreativeTab;
import ru.blatfan.blatapi.common.creativetab.SubCreativeTabStack;
import ru.blatfan.blatapi.common.guide_book.GuideBookItem;
import ru.blatfan.blatapi.common.guide_book.GuidePaperItem;
import ru.blatfan.blatapi.common.item.TestStickItem;
import ru.blatfan.blatapi.common.loot.AddItemListLootModifier;
import ru.blatfan.blatapi.common.loot.AddItemLootModifier;
import ru.blatfan.blatapi.common.loot.AddLootTableModifier;
import ru.blatfan.blatapi.common.mobeffect.*;
import ru.blatfan.blatapi.common.recipe.AnvilRecipe;
import ru.blatfan.blatapi.common.recipe.AnvilRepairRecipe;
import ru.blatfan.blatapi.common.recipe.IAnvilRecipe;
import ru.blatfan.blatapi.common.recipe.IAnvilRepairRecipe;
import ru.blatfan.blatapi.common.registry.BlatRegister;
import ru.blatfan.blatapi.utils.NBTHelper;
import ru.blatfan.blatapi.utils.RegistryUtils;
import ru.blatfan.blatapi.utils.collection.Text;

import java.util.Collection;
import java.util.function.Supplier;

public class BARegistry {
    public static final BlatRegister REG = new BlatRegister(BlatApi.MOD_ID);
    
    public static final RegistryObject<RecipeType<IAnvilRecipe>> ANVIL = REG.recipe_type(AnvilRecipe.type.getPath());
    public static final RegistryObject<RecipeType<IAnvilRepairRecipe>> ANVIL_REPAIR = REG.recipe_type(AnvilRepairRecipe.type.getPath());
    
    public static final RegistryObject<RecipeSerializer<IAnvilRecipe>> ANVIL_S = REG.recipe_serializer(AnvilRecipe.type.getPath(), AnvilRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<IAnvilRepairRecipe>> ANVIL_REPAIR_S = REG.recipe_serializer(AnvilRepairRecipe.type.getPath(), AnvilRepairRecipe.Serializer::new);
    
    public static class MobEffects {
        public static final RegistryObject<MobEffect> ATTACK_SPEED = REG.mob_effect("attack_speed", AttackSpeedMobEffect::new);
        public static final RegistryObject<MobEffect> ATTACK_SLOWDOWN = REG.mob_effect("attack_slowdown", AttackSlownessMobEffect::new);
        public static final RegistryObject<MobEffect> ATTACK_KNOCKBACK_STRENGTH = REG.mob_effect("attack_knockback_strength", AttackKnockbackStrengthMobEffect::new);
        public static final RegistryObject<MobEffect> ATTACK_KNOCKBACK_WEAKNESS = REG.mob_effect("attack_knockback_weakness", AttackKnockbackWeaknessMobEffect::new);
        public static final RegistryObject<MobEffect> ARMOR_STRENGTH = REG.mob_effect("armor_strength", ArmorStrengthMobEffect::new);
        public static final RegistryObject<MobEffect> ARMOR_WEAKNESS = REG.mob_effect("armor_weakness", ArmorWeaknessMobEffect::new);
        public static final RegistryObject<MobEffect> ARMOR_TOUGHNESS_STRENGTH = REG.mob_effect("armor_toughness_strength", ArmorToughnessStrengthMobEffect::new);
        public static final RegistryObject<MobEffect> ARMOR_TOUGHNESS_WEAKNESS = REG.mob_effect("armor_toughness_weakness", ArmorToughnessWeaknessMobEffect::new);
        public static final RegistryObject<MobEffect> KNOCKBACK_RESISTANCE = REG.mob_effect("knockback_resistance", KnockbackResistanceMobEffect::new);
        public static final RegistryObject<MobEffect> KNOCKBACK_SENSIBILITY = REG.mob_effect("knockback_sensibility", KnockbackSensibilityMobEffect::new);
        public static final RegistryObject<MobEffect> ENTITY_REACH_EXPAND = REG.mob_effect("entity_reach_expand", EntityReachExpandMobEffect::new);
        public static final RegistryObject<MobEffect> ENTITY_REACH_SHRINK = REG.mob_effect("entity_reach_shrink", EntityReachShrinkMobEffect::new);
        public static final RegistryObject<MobEffect> BLOCK_REACH_EXPAND = REG.mob_effect("block_reach_expand", BlockReachExpandMobEffect::new);
        public static final RegistryObject<MobEffect> BLOCK_REACH_SHRINK = REG.mob_effect("block_reach_shrink", BlockReachShrinkMobEffect::new);
        public static final RegistryObject<MobEffect> SWIM_SPEED = REG.mob_effect("swim_speed", SwimSpeedMobEffect::new);
        public static final RegistryObject<MobEffect> SWIM_SLOWNESS = REG.mob_effect("swim_slowness", SwimSlownessMobEffect::new);
        
        static void init(){}
    }
    
    public static class Potions {
        public static final RegistryObject<Potion> ATTACK_SPEED = REG.potion("attack_speed", ()-> new Potion(new MobEffectInstance(MobEffects.ATTACK_SPEED.get(), 600, 0)));
        public static final RegistryObject<Potion> LONG_ATTACK_SPEED = REG.potion("long_attack_speed", ()-> new Potion(new MobEffectInstance(MobEffects.ATTACK_SPEED.get(), 1200, 1)));
        
        public static final RegistryObject<Potion> ATTACK_SLOWDOWN = REG.potion("attack_slowdown", ()-> new Potion(new MobEffectInstance(MobEffects.ATTACK_SLOWDOWN.get(), 600, 0)));
        public static final RegistryObject<Potion> LONG_ATTACK_SLOWDOWN = REG.potion("long_attack_slowdown", ()-> new Potion(new MobEffectInstance(MobEffects.ATTACK_SLOWDOWN.get(), 1200, 1)));
        
        public static final RegistryObject<Potion> ATTACK_KNOCKBACK_STRENGTH = REG.potion("attack_knockback_strength", ()-> new Potion(new MobEffectInstance(MobEffects.ATTACK_KNOCKBACK_STRENGTH.get(), 600, 0)));
        public static final RegistryObject<Potion> LONG_ATTACK_KNOCKBACK_STRENGTH = REG.potion("long_attack_knockback_strength", ()-> new Potion(new MobEffectInstance(MobEffects.ATTACK_KNOCKBACK_STRENGTH.get(), 1200, 10)));
        
        public static final RegistryObject<Potion> ATTACK_KNOCKBACK_WEAKNESS = REG.potion("attack_knockback_weakness", ()-> new Potion(new MobEffectInstance(MobEffects.ATTACK_KNOCKBACK_WEAKNESS.get(), 600, 0)));
        public static final RegistryObject<Potion> LONG_ATTACK_KNOCKBACK_WEAKNESS = REG.potion("long_attack_knockback_weakness", ()-> new Potion(new MobEffectInstance(MobEffects.ATTACK_KNOCKBACK_WEAKNESS.get(), 1200, 10)));
        
        public static final RegistryObject<Potion> ARMOR_STRENGTH = REG.potion("armor_strength", ()-> new Potion(new MobEffectInstance(MobEffects.ARMOR_STRENGTH.get(), 600, 0)));
        public static final RegistryObject<Potion> LONG_ARMOR_STRENGTH = REG.potion("long_armor_strength", ()-> new Potion(new MobEffectInstance(MobEffects.ARMOR_STRENGTH.get(), 1200, 1)));
        
        public static final RegistryObject<Potion> ARMOR_WEAKNESS = REG.potion("armor_weakness", ()-> new Potion(new MobEffectInstance(MobEffects.ARMOR_WEAKNESS.get(), 600, 0)));
        public static final RegistryObject<Potion> LONG_ARMOR_WEAKNESS = REG.potion("long_armor_weakness", ()-> new Potion(new MobEffectInstance(MobEffects.ARMOR_WEAKNESS.get(), 1200, 1)));
        
        public static final RegistryObject<Potion> ARMOR_TOUGHNESS_STRENGTH = REG.potion("armor_toughness_strength", ()-> new Potion(new MobEffectInstance(MobEffects.ARMOR_TOUGHNESS_STRENGTH.get(), 600, 0)));
        public static final RegistryObject<Potion> LONG_ARMOR_TOUGHNESS_STRENGTH = REG.potion("long_armor_toughness_strength", ()-> new Potion(new MobEffectInstance(MobEffects.ARMOR_TOUGHNESS_STRENGTH.get(), 1200, 1)));
        
        public static final RegistryObject<Potion> ARMOR_TOUGHNESS_WEAKNESS = REG.potion("armor_toughness_weakness", ()-> new Potion(new MobEffectInstance(MobEffects.ARMOR_TOUGHNESS_WEAKNESS.get(), 600, 0)));
        public static final RegistryObject<Potion> LONG_ARMOR_TOUGHNESS_WEAKNESS = REG.potion("long_armor_toughness_weakness", ()-> new Potion(new MobEffectInstance(MobEffects.ARMOR_TOUGHNESS_WEAKNESS.get(), 1200, 1)));
        
        public static final RegistryObject<Potion> KNOCKBACK_RESISTANCE = REG.potion("knockback_resistance", ()-> new Potion(new MobEffectInstance(MobEffects.KNOCKBACK_RESISTANCE.get(), 600, 0)));
        public static final RegistryObject<Potion> LONG_KNOCKBACK_RESISTANCE = REG.potion("long_knockback_resistance", ()-> new Potion(new MobEffectInstance(MobEffects.KNOCKBACK_RESISTANCE.get(), 1200, 1)));
        
        public static final RegistryObject<Potion> KNOCKBACK_SENSIBILITY = REG.potion("knockback_sensibility", ()-> new Potion(new MobEffectInstance(MobEffects.KNOCKBACK_SENSIBILITY.get(), 600, 0)));
        public static final RegistryObject<Potion> LONG_KNOCKBACK_SENSIBILITY = REG.potion("long_knockback_sensibility", ()-> new Potion(new MobEffectInstance(MobEffects.KNOCKBACK_SENSIBILITY.get(), 1200, 1)));
        
        public static final RegistryObject<Potion> ENTITY_REACH_EXPAND = REG.potion("entity_reach_expand", ()-> new Potion(new MobEffectInstance(MobEffects.ENTITY_REACH_EXPAND.get(), 600, 0)));
        public static final RegistryObject<Potion> LONG_ENTITY_REACH_EXPAND = REG.potion("long_entity_reach_expand", ()-> new Potion(new MobEffectInstance(MobEffects.ENTITY_REACH_EXPAND.get(), 1200, 1)));
        
        public static final RegistryObject<Potion> ENTITY_REACH_SHRINK = REG.potion("entity_reach_shrink", ()-> new Potion(new MobEffectInstance(MobEffects.ENTITY_REACH_SHRINK.get(), 600, 0)));
        public static final RegistryObject<Potion> LONG_ENTITY_REACH_SHRINK = REG.potion("long_entity_reach_shrink", ()-> new Potion(new MobEffectInstance(MobEffects.ENTITY_REACH_SHRINK.get(), 1200, 1)));
        
        public static final RegistryObject<Potion> BLOCK_REACH_EXPAND = REG.potion("block_reach_expand", ()-> new Potion(new MobEffectInstance(MobEffects.BLOCK_REACH_EXPAND.get(), 600, 0)));
        public static final RegistryObject<Potion> LONG_BLOCK_REACH_EXPAND = REG.potion("long_block_reach_expand", ()-> new Potion(new MobEffectInstance(MobEffects.BLOCK_REACH_EXPAND.get(), 1200, 1)));
        
        public static final RegistryObject<Potion> BLOCK_REACH_SHRINK = REG.potion("block_reach_shrink", ()-> new Potion(new MobEffectInstance(MobEffects.BLOCK_REACH_SHRINK.get(), 600, 0)));
        public static final RegistryObject<Potion> LONG_BLOCK_REACH_SHRINK = REG.potion("long_block_reach_shrink", ()-> new Potion(new MobEffectInstance(MobEffects.BLOCK_REACH_SHRINK.get(), 1200, 1)));
        
        public static final RegistryObject<Potion> SWIM_SPEED = REG.potion("swim_speed", ()-> new Potion(new MobEffectInstance(MobEffects.SWIM_SPEED.get(), 600, 0)));
        public static final RegistryObject<Potion> LONG_SWIM_SPEED = REG.potion("long_swim_speed", ()-> new Potion(new MobEffectInstance(MobEffects.SWIM_SPEED.get(), 1200, 1)));
        
        public static final RegistryObject<Potion> SWIM_SLOWNESS = REG.potion("swim_slowness", ()-> new Potion(new MobEffectInstance(MobEffects.SWIM_SLOWNESS.get(), 600, 0)));
        public static final RegistryObject<Potion> LONG_SWIM_SLOWNESS = REG.potion("long_swim_slowness", ()-> new Potion(new MobEffectInstance(MobEffects.SWIM_SLOWNESS.get(), 1200, 1)));
        
        static void init(){}
    }
    
    public static class Fonts {
        public static ResourceLocation ICONS_LOCATION = BlatApi.loc("icons");
        public static Font ICONS = FontHandler.createFont(ICONS_LOCATION);
        
        static void init(){}
    }
    
    public static class Items {
        public static final RegistryObject<Item> TEST_STICK = REG.item("test_stick", TestStickItem::new);
        public static final RegistryObject<Item> GUIDE_BOOK = REG.item("guide_book", GuideBookItem::new);
        public static final RegistryObject<Item> QUESTION = REG.singleItem("question");
        public static final RegistryObject<Item> RED_BOOK = REG.singleItem("red_book");
        public static final RegistryObject<Item> BLUE_BOOK = REG.singleItem("blue_book");
        public static final RegistryObject<Item> GREEN_BOOK = REG.singleItem("green_book");
        public static final RegistryObject<Item> YELLOW_BOOK = REG.singleItem("yellow_book");
        public static final RegistryObject<Item> PINK_BOOK = REG.singleItem("pink_book");
        public static final RegistryObject<Item> PURPLE_BOOK = REG.singleItem("purple_book");
        public static final RegistryObject<Item> ORANGE_BOOK = REG.singleItem("orange_book");
        public static final RegistryObject<Item> ERROR_BOOK = REG.item("error_book", () -> new GuideBookItem.Transfer(BlatApi.loc("error")));
        public static final RegistryObject<Item> GUIDE_BOOK_PAPER = REG.item("guide_book_paper", GuidePaperItem::new);
        
        static void init(){}
    }
    
    public static class CreativeTabs {
        private static final Text TITLE = Text.create(BlatApi.MOD_NAME);
        
        public static RegistryObject<CreativeModeTab> TAB = REG.creative_mode_tab("main", ()-> MultiCreativeTab.builder()
            .icon(()->new ItemStack(net.minecraft.world.item.Items.AMETHYST_SHARD)).title(TITLE.copyText()).multiBuild());
        
        public static final SubCreativeTabStack ALL =
            SubCreativeTabStack.create().subTitle(TITLE.copyText());
        
        public static final SubCreativeTab POTIONS =
            SubCreativeTab.create().subIcon(() -> {
                    ItemStack stack = new ItemStack(net.minecraft.world.item.Items.POTION);
                    NBTHelper.setInt(stack, PotionUtils.TAG_CUSTOM_POTION_COLOR, POTION_RAINBOW_COLOR);
                    return stack;
                })
                .title(TITLE.copyText().add(": ").add("creative_tab.potions"))
                .subTitle(Text.create("creative_tab.potions"));
        
        public static final SubCreativeTab BOOKS =
            SubCreativeTab.create().subIcon(() -> new ItemStack(net.minecraft.world.item.Items.BOOK))
                .title(TITLE.copyText().add(": ").add("creative_tab.books"))
                .subTitle(Text.create("creative_tab.books"));
        
        public static final SubCreativeTab DEBUG =
            SubCreativeTab.create().subIcon(() -> new ItemStack(net.minecraft.world.item.Items.COMMAND_BLOCK))
                .title(TITLE.copyText().add(": ").add("creative_tab.debug"))
                .subTitle(Text.create("creative_tab.debug"));
        
        public static void addSubTabs(){
            if(TAB.get() instanceof MultiCreativeTab multiCreativeTab){
                multiCreativeTab.addSubTab(ALL)
                .addSubTab(BOOKS)
                .addSubTab(POTIONS)
                .addSubTab(DEBUG);
            }
        }
        
        public static void addCreativeTabContent(BuildCreativeModeTabContentsEvent event) {
            if (event.getTabKey() == TAB.getKey()) {
                for (ResourceLocation guideBookData : GuideManager.books().keySet()) {
                    if(GuideManager.books().get(guideBookData).isToRegistry())
                        addInSub(event, BOOKS, GuideBookItem.getBook(guideBookData));
                }
                if(event.hasPermissions()){
                    addInSub(event, DEBUG, Items.TEST_STICK);
                }
                for(RegistryObject<Potion> potion : REG.POTIONS.getEntries()) {
                    addInSub(event, POTIONS, PotionUtils.setPotion(net.minecraft.world.item.Items.POTION.getDefaultInstance(), potion.get()));
                    addInSub(event, POTIONS, PotionUtils.setPotion(net.minecraft.world.item.Items.SPLASH_POTION.getDefaultInstance(), potion.get()));
                    addInSub(event, POTIONS, PotionUtils.setPotion(net.minecraft.world.item.Items.LINGERING_POTION.getDefaultInstance(), potion.get()));
                    addInSub(event, POTIONS, PotionUtils.setPotion(net.minecraft.world.item.Items.TIPPED_ARROW.getDefaultInstance(), potion.get()));
                }
            }
            if (event.getTabKey() == CreativeModeTabs.OP_BLOCKS && event.hasPermissions()) {
                event.accept(Items.TEST_STICK);
            }
        }
        
        public static void addInSub(BuildCreativeModeTabContentsEvent event, SubCreativeTab subTab, Supplier<? extends ItemLike> item) {
            event.accept(item);
            subTab.addDisplayItem(item.get());
        }
        
        public static void addInSub(BuildCreativeModeTabContentsEvent event, SubCreativeTab subTab, ItemStack item) {
            event.accept(item);
            subTab.addDisplayItem(item);
        }
        
        public static void addInSub(BuildCreativeModeTabContentsEvent event, SubCreativeTab subTab, Collection<ItemStack> items) {
            event.acceptAll(items);
            subTab.addDisplayItems(items);
        }
        static void init(){}
    }
    
    public static class LootModifiers {
        public static final RegistryObject<Codec<AddItemLootModifier>> ADD_ITEM = REG.loot_modifier("add_item", AddItemLootModifier.CODEC);
        public static final RegistryObject<Codec<AddItemListLootModifier>> ADD_ITEM_LIST = REG.loot_modifier("add_item_list", AddItemListLootModifier.CODEC);
        public static final RegistryObject<Codec<AddLootTableModifier>> ADD_LOOT_TABLE = REG.loot_modifier("add_loot_table", AddLootTableModifier.CODEC);
        
        static void init(){}
    }
    
    public static class BlockEntities {
        public static final RegistryObject<BlockEntityType<CustomSignBlockEntity>> SIGN = REG.block_entity_type("sign", () -> BlockEntityType.Builder.of(CustomSignBlockEntity::new, RegistryUtils.getBlocks(CustomStandingSignBlock.class, CustomWallSignBlock.class)).build(null));
        public static final RegistryObject<BlockEntityType<CustomHangingSignBlockEntity>> HANGING_SIGN = REG.block_entity_type("hanging_sign", () -> BlockEntityType.Builder.of(CustomHangingSignBlockEntity::new, RegistryUtils.getBlocks(CustomCeilingHangingSignBlock.class, CustomWallHangingSignBlock.class)).build(null));
        
        static void init(){}
    }
    
    public static class Particles {
        public static RegistryObject<GenericParticleType> EMERALD = REG.particle_type("emerald", GenericParticleType::new);
        public static RegistryObject<GenericParticleType> WISP = REG.particle_type("wisp", GenericParticleType::new);
        public static RegistryObject<GenericParticleType> TINY_WISP = REG.particle_type("tiny_wisp", GenericParticleType::new);
        public static RegistryObject<GenericParticleType> SPARKLE = REG.particle_type("sparkle", GenericParticleType::new);
        public static RegistryObject<GenericParticleType> STAR = REG.particle_type("star", GenericParticleType::new);
        public static RegistryObject<GenericParticleType> TINY_STAR = REG.particle_type("tiny_star", GenericParticleType::new);
        public static RegistryObject<GenericParticleType> FIRE = REG.particle_type("fire", GenericParticleType::new);
        public static RegistryObject<GenericParticleType> SQUARE = REG.particle_type("square", GenericParticleType::new);
        public static RegistryObject<GenericParticleType> DOT = REG.particle_type("dot", GenericParticleType::new);
        public static RegistryObject<GenericParticleType> CIRCLE = REG.particle_type("circle", GenericParticleType::new);
        public static RegistryObject<GenericParticleType> TINY_CIRCLE = REG.particle_type("tiny_circle", GenericParticleType::new);
        public static RegistryObject<GenericParticleType> HEART = REG.particle_type("heart", GenericParticleType::new);
        public static RegistryObject<GenericParticleType> SKULL = REG.particle_type("skull", GenericParticleType::new);
        public static RegistryObject<GenericParticleType> SMOKE = REG.particle_type("smoke", GenericParticleType::new);
        public static RegistryObject<GenericParticleType> TRAIL = REG.particle_type("trail", GenericParticleType::new);
        public static RegistryObject<GenericParticleType> PANCAKE = REG.particle_type("pancake", GenericParticleType::new);
        public static RegistryObject<GenericParticleType> DEATH = REG.particle_type("death", GenericParticleType::new);
        public static RegistryObject<GenericParticleType> EARTH = REG.particle_type("earth", GenericParticleType::new);
        public static RegistryObject<GenericParticleType> SUN = REG.particle_type("sun", GenericParticleType::new);
        public static RegistryObject<ItemParticleType> ITEM = REG.particle_type("item", ItemParticleType::new);
        public static RegistryObject<BlockParticleType> BLOCK = REG.particle_type("block", BlockParticleType::new);
        public static RegistryObject<FluidParticleType> FLUID = REG.particle_type("fluid", FluidParticleType::new);
        public static RegistryObject<SpriteParticleType> SPRITE = REG.particle_type("sprite", SpriteParticleType::new);
        public static RegistryObject<LeavesParticleType> CHERRY_LEAVES = REG.particle_type("cherry_leaves", LeavesParticleType::new);
        
        static void init(){}
    }
    
    public static final int POTION_RAINBOW_COLOR = -676767;
    public static final TagKey<DamageType> MAGIC = TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("forge", "is_magic"));
    
    public static void register(IEventBus event){
        Items.init();
        MobEffects.init();
        Potions.init();
        CreativeTabs.init();
        Fonts.init();
        LootModifiers.init();
        BlockEntities.init();
        Particles.init();
        REG.register(event);
    }
}