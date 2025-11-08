package ru.blatfan.blatapi.common;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.common.guide_book.GuideBookItem;
import ru.blatfan.blatapi.common.recipe.AnvilRecipe;
import ru.blatfan.blatapi.common.recipe.AnvilRepairRecipe;
import ru.blatfan.blatapi.common.recipe.IAnvilRecipe;
import ru.blatfan.blatapi.common.recipe.IAnvilRepairRecipe;
import ru.blatfan.blatapi.common.registry.BlatRegister;
import ru.blatfan.blatapi.fluffy_fur.common.creativetab.MultiCreativeTab;
import ru.blatfan.blatapi.fluffy_fur.common.creativetab.SubCreativeTab;
import ru.blatfan.blatapi.fluffy_fur.common.creativetab.SubCreativeTabStack;
import ru.blatfan.blatapi.fluffy_fur.common.mobeffect.*;
import ru.blatfan.blatapi.fluffy_fur.registry.common.item.FluffyFurItems;
import ru.blatfan.blatapi.utils.NBTHelper;
import ru.blatfan.blatapi.utils.collection.Text;

import java.util.Collection;
import java.util.function.Supplier;

public class BARegistry {
    public static final BlatRegister REG = new BlatRegister(BlatApi.MOD_ID);
    
    public static final RegistryObject<RecipeType<IAnvilRecipe>> ANVIL = REG.recipe_type(AnvilRecipe.type.getPath());
    public static final RegistryObject<RecipeType<IAnvilRepairRecipe>> ANVIL_REPAIR = REG.recipe_type(AnvilRepairRecipe.type.getPath());
    
    public static final RegistryObject<RecipeSerializer<IAnvilRecipe>> ANVIL_S = REG.recipe_serializer(AnvilRecipe.type.getPath(), AnvilRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<IAnvilRepairRecipe>> ANVIL_REPAIR_S = REG.recipe_serializer(AnvilRepairRecipe.type.getPath(), AnvilRepairRecipe.Serializer::new);
    
    public static final RegistryObject<Item> GUIDE_BOOK = REG.item("guide_book", GuideBookItem::new);
    
    public static class MobEffects {
        public static final RegistryObject<MobEffect> ATTACK_SPEED = REG.mob_effect("attack_speed", AttackSpeedMobEffect::new);
        public static final RegistryObject<MobEffect> ATTACK_SLOWNESS = REG.mob_effect("attack_slowness", AttackSlownessMobEffect::new);
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
        
        public static void init(){}
    }
    
    public static class Potions {
        public static final RegistryObject<Potion> ATTACK_SPEED = REG.potion("attack_speed", ()-> new Potion(new MobEffectInstance(MobEffects.ATTACK_SPEED.get(), 600, 0)));
        public static final RegistryObject<Potion> LONG_ATTACK_SPEED = REG.potion("long_attack_speed", ()-> new Potion(new MobEffectInstance(MobEffects.ATTACK_SPEED.get(), 1200, 1)));
        
        public static final RegistryObject<Potion> ATTACK_SLOWDOWN = REG.potion("attack_slowdown", ()-> new Potion(new MobEffectInstance(MobEffects.ATTACK_SLOWNESS.get(), 600, 0)));
        public static final RegistryObject<Potion> LONG_ATTACK_SLOWDOWN = REG.potion("long_attack_slowdown", ()-> new Potion(new MobEffectInstance(MobEffects.ATTACK_SLOWNESS.get(), 1200, 1)));
        
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
        
        public static void init(){}
    }
    
    public static class CreativeTabs {
        private static final Text TITLE = Text.create(BlatApi.MOD_NAME);
        
        public static RegistryObject<CreativeModeTab> TAB = REG.creative_mode_tab("main", ()-> MultiCreativeTab.builder()
            .icon(()->new ItemStack(Items.AMETHYST_SHARD)).title(TITLE).multiBuild());
        
        public static final SubCreativeTabStack ALL =
            SubCreativeTabStack.create().subTitle(TITLE);
        
        public static final SubCreativeTab POTIONS =
            SubCreativeTab.create().subIcon(() -> {
                    ItemStack stack = new ItemStack(Items.POTION);
                    NBTHelper.setInt(stack, PotionUtils.TAG_CUSTOM_POTION_COLOR, POTION_RAINBOW_COLOR);
                    return stack;
                })
                .title(TITLE.copyText().add(": ").add("creative_tab.potions"))
                .subTitle(Component.translatable("creative_tab.potions"));
        
        public static final SubCreativeTab BOOKS =
            SubCreativeTab.create().subIcon(() -> new ItemStack(Items.BOOK))
                .title(TITLE.copyText().add(": ").add("creative_tab.books"))
                .subTitle(Component.translatable("creative_tab.books"));
        
        public static final SubCreativeTab DEBUG =
            SubCreativeTab.create().subIcon(() -> new ItemStack(Items.COMMAND_BLOCK))
                .title(TITLE.copyText().add(": ").add("creative_tab.debug"))
                .subTitle(Component.translatable("creative_tab.debug"));
        
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
                    addInSub(event, DEBUG, FluffyFurItems.TEST_STICK);
                }
                for(RegistryObject<Potion> potion : REG.POTIONS.getEntries()) {
                    addInSub(event, POTIONS, PotionUtils.setPotion(Items.POTION.getDefaultInstance(), potion.get()));
                    addInSub(event, POTIONS, PotionUtils.setPotion(Items.SPLASH_POTION.getDefaultInstance(), potion.get()));
                    addInSub(event, POTIONS, PotionUtils.setPotion(Items.LINGERING_POTION.getDefaultInstance(), potion.get()));
                    addInSub(event, POTIONS, PotionUtils.setPotion(Items.TIPPED_ARROW.getDefaultInstance(), potion.get()));
                }
            }
            if (event.getTabKey() == CreativeModeTabs.OP_BLOCKS && event.hasPermissions()) {
                event.accept(FluffyFurItems.TEST_STICK);
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
        public static void init(){}
    }
    
    public static final int POTION_RAINBOW_COLOR = -676767;
    
    public static void register(IEventBus event){
        MobEffects.init();
        Potions.init();
        CreativeTabs.init();
        REG.register(event);
    }
}