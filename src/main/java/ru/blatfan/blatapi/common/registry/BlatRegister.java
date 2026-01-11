package ru.blatfan.blatapi.common.registry;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

// TODO через RegisterEvent
public class BlatRegister {
    public final String modid;
    public final DeferredRegister<Block> BLOCKS;
    public final DeferredRegister<Fluid> FLUIDS;
    public final DeferredRegister<FluidType> FLUID_TYPES;
    public final DeferredRegister<Item> ITEMS;
    public final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB;
    public final DeferredRegister<MobEffect> MOB_EFFECTS;
    public final DeferredRegister<SoundEvent> SOUND_EVENTS;
    public final DeferredRegister<Potion> POTIONS;
    public final DeferredRegister<Enchantment> ENCHANTMENTS;
    public final DeferredRegister<EntityType<?>> ENTITY_TYPES;
    public final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES;
    public final DeferredRegister<ParticleType<?>> PARTICLE_TYPES;
    public final DeferredRegister<MenuType<?>> MENU_TYPES;
    public final DeferredRegister<PaintingVariant> PAINTING_VARIANTS;
    public final DeferredRegister<RecipeType<?>> RECIPE_TYPES;
    public final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS;
    public final DeferredRegister<Attribute> ATTRIBUTES;
    public final DeferredRegister<StatType<?>> STAT_TYPES;
    public final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES;
    public final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS;
    public final DeferredRegister<PoiType> POI_TYPES;
    public final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES;
    public final DeferredRegister<SensorType<?>> SENSOR_TYPES;
    public final DeferredRegister<Schedule> SCHEDULES;
    public final DeferredRegister<Activity> ACTIVITIES;
    public final DeferredRegister<WorldCarver<?>> WORLD_CARVERS;
    public final DeferredRegister<Feature<?>> FEATURES;
    public final DeferredRegister<ChunkStatus> CHUNK_STATUS;
    public final DeferredRegister<BlockStateProviderType<?>> BLOCK_STATE_PROVIDER_TYPES;
    public final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPES;
    public final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATOR_TYPES;
    public final DeferredRegister<Biome> BIOMES;
    public final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACER_TYPE;
    
    public BlatRegister(String modid) {
        this.modid = modid;
        
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, modid);
        FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, modid);
        FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, modid);
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, modid);
        CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, modid);
        MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, modid);
        SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, modid);
        POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, modid);
        ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, modid);
        ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, modid);
        BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, modid);
        PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, modid);
        MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, modid);
        PAINTING_VARIANTS = DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, modid);
        RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, modid);
        RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, modid);
        ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, modid);
        STAT_TYPES = DeferredRegister.create(ForgeRegistries.STAT_TYPES, modid);
        COMMAND_ARGUMENT_TYPES = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, modid);
        VILLAGER_PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, modid);
        POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, modid);
        MEMORY_MODULE_TYPES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, modid);
        SENSOR_TYPES = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, modid);
        SCHEDULES = DeferredRegister.create(ForgeRegistries.SCHEDULES, modid);
        ACTIVITIES = DeferredRegister.create(ForgeRegistries.ACTIVITIES, modid);
        WORLD_CARVERS = DeferredRegister.create(ForgeRegistries.WORLD_CARVERS, modid);
        FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, modid);
        CHUNK_STATUS = DeferredRegister.create(ForgeRegistries.CHUNK_STATUS, modid);
        BLOCK_STATE_PROVIDER_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_STATE_PROVIDER_TYPES, modid);
        FOLIAGE_PLACER_TYPES = DeferredRegister.create(ForgeRegistries.FOLIAGE_PLACER_TYPES, modid);
        TREE_DECORATOR_TYPES = DeferredRegister.create(ForgeRegistries.TREE_DECORATOR_TYPES, modid);
        BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, modid);
        TRUNK_PLACER_TYPE = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, modid);
    }
    
    public void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
        FLUIDS.register(eventBus);
        FLUID_TYPES.register(eventBus);
        ITEMS.register(eventBus);
        CREATIVE_MODE_TAB.register(eventBus);
        MOB_EFFECTS.register(eventBus);
        SOUND_EVENTS.register(eventBus);
        POTIONS.register(eventBus);
        ENCHANTMENTS.register(eventBus);
        ENTITY_TYPES.register(eventBus);
        BLOCK_ENTITY_TYPES.register(eventBus);
        PARTICLE_TYPES.register(eventBus);
        MENU_TYPES.register(eventBus);
        PAINTING_VARIANTS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
        ATTRIBUTES.register(eventBus);
        STAT_TYPES.register(eventBus);
        COMMAND_ARGUMENT_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
        POI_TYPES.register(eventBus);
        MEMORY_MODULE_TYPES.register(eventBus);
        SENSOR_TYPES.register(eventBus);
        SCHEDULES.register(eventBus);
        ACTIVITIES.register(eventBus);
        WORLD_CARVERS.register(eventBus);
        FEATURES.register(eventBus);
        CHUNK_STATUS.register(eventBus);
        BLOCK_STATE_PROVIDER_TYPES.register(eventBus);
        FOLIAGE_PLACER_TYPES.register(eventBus);
        TREE_DECORATOR_TYPES.register(eventBus);
        BIOMES.register(eventBus);
        TRUNK_PLACER_TYPE.register(eventBus);
    }
    
    public <T extends Block> RegistryObject<T> block(String id, Supplier<T> supplier){
        return BLOCKS.register(id, supplier);
    }
    
    public RegistryObject<Block> block(String id){
        return block(id, ()-> new Block(BlockBehaviour.Properties.of()));
    }
    
    public <T extends Fluid> RegistryObject<T> fluid(String id, Supplier<T> supplier){
        return FLUIDS.register(id, supplier);
    }
    
    public <T extends FluidType> RegistryObject<T> fluid_type(String id, Supplier<T> supplier){
        return FLUID_TYPES.register(id, supplier);
    }
    
    public <T extends Item> RegistryObject<T> item(String id, Supplier<T> supplier){
        return ITEMS.register(id, supplier);
    }
    public RegistryObject<Item> item(String id){
        return item(id, ()-> new Item(new Item.Properties()));
    }
    
    public RegistryObject<CreativeModeTab> creative_mode_tab(String id, Supplier<CreativeModeTab> supplier){
        return CREATIVE_MODE_TAB.register(id, supplier);
    }
    
    public RegistryObject<MobEffect> mob_effect(String id, Supplier<MobEffect> supplier){
        return MOB_EFFECTS.register(id, supplier);
    }
    
    public RegistryObject<SoundEvent> sound_event(String id, Supplier<SoundEvent> supplier){
        return SOUND_EVENTS.register(id, supplier);
    }
    public RegistryObject<SoundEvent> sound_event(String name){
        return sound_event(name, ()-> SoundEvent.createVariableRangeEvent(new ResourceLocation(modid, name)));
    }
    
    public RegistryObject<Potion> potion(String id, Supplier<Potion> supplier){
        return POTIONS.register(id, supplier);
    }
    
    public RegistryObject<Enchantment> enchantment(String id, Supplier<Enchantment> supplier){
        return ENCHANTMENTS.register(id, supplier);
    }
    
    public <T extends EntityType<?>> RegistryObject<T> entity_type(String id, Supplier<T> supplier){
        return ENTITY_TYPES.register(id, supplier);
    }
    public <T extends Entity> RegistryObject<EntityType<T>> entity_type(String name, MobCategory mobCategory, float width, float height, int trackingRange, EntityType.EntityFactory<T> factory) {
        return entity_type(name, mobCategory, width, height, trackingRange, 1, factory);
    }
    public <T extends Entity> RegistryObject<EntityType<T>> entity_type(String name, MobCategory mobCategory, float width, float height, int trackingRange, int updateInterval, EntityType.EntityFactory<T> factory) {
        return entity_type(name, ()->EntityType.Builder.of(factory, mobCategory)
            .setTrackingRange(trackingRange)
            .setUpdateInterval(updateInterval)
            .sized(width, height)
            .build(modid + ":" + name));
    }
    
    public <T extends BlockEntityType<?>> RegistryObject<T> block_entity_type(String id, Supplier<T> supplier){
        return BLOCK_ENTITY_TYPES.register(id, supplier);
    }
    
    public <T extends BlockEntity> RegistryObject<BlockEntityType<?>> block_entity_type(String id, BlockEntityType.BlockEntitySupplier<T> pFactory, Block... pValidBlocks){
        return block_entity_type(id, () -> BlockEntityType.Builder.of(pFactory, pValidBlocks).build(null));
    }
    
    public <T extends ParticleType<?>> RegistryObject<T> particle_type(String id, Supplier<T> supplier){
        return PARTICLE_TYPES.register(id, supplier);
    }
    
    public <T extends MenuType<?>> RegistryObject<T> menu_type(String id, Supplier<T> supplier){
        return MENU_TYPES.register(id, supplier);
    }
    
    public RegistryObject<PaintingVariant> painting_variant(String id, Supplier<PaintingVariant> supplier){
        return PAINTING_VARIANTS.register(id, supplier);
    }
    
    public <T extends RecipeType<?>> RegistryObject<T> recipe_type(String id, Supplier<T> supplier){
        return RECIPE_TYPES.register(id, supplier);
    }
    public <T extends Recipe<?>> RegistryObject<RecipeType<T>> recipe_type(String id){
        return recipe_type(id, ()-> RecipeType.simple(new ResourceLocation(modid, id)));
    }
    
    public <T extends RecipeSerializer<?>> RegistryObject<T> recipe_serializer(String id, Supplier<T> supplier){
        return RECIPE_SERIALIZERS.register(id, supplier);
    }
    
    public RegistryObject<Attribute> attribute(String id, Supplier<Attribute> supplier){
        return ATTRIBUTES.register(id, supplier);
    }
    public RegistryObject<Attribute> attribute(String id, double defValue, double minValue, double maxValue){
        return attribute(id, ()-> new RangedAttribute(id, defValue, minValue, maxValue));
    }
    public RegistryObject<Attribute> attribute(String id, double defValue, double minValue){
        return attribute(id, defValue, minValue, Integer.MAX_VALUE);
    }
    public RegistryObject<Attribute> attribute(String id, double defValue){
        return attribute(id, defValue, 0, Integer.MAX_VALUE);
    }
    
    public RegistryObject<StatType<?>> stat_type(String id, Supplier<StatType<?>> supplier){
        return STAT_TYPES.register(id, supplier);
    }
    
    public RegistryObject<ArgumentTypeInfo<?,?>> command_argument_type(String id, Supplier<ArgumentTypeInfo<?,?>> supplier){
        return COMMAND_ARGUMENT_TYPES.register(id, supplier);
    }
    
    public RegistryObject<VillagerProfession> villager_profession(String id, Supplier<VillagerProfession> supplier){
        return VILLAGER_PROFESSIONS.register(id, supplier);
    }
    
    public RegistryObject<PoiType> poi_type(String id, Supplier<PoiType> supplier){
        return POI_TYPES.register(id, supplier);
    }
    
    public RegistryObject<MemoryModuleType<?>> memory_module_type(String id, Supplier<MemoryModuleType<?>> supplier){
        return MEMORY_MODULE_TYPES.register(id, supplier);
    }
    
    public RegistryObject<SensorType<?>> sensor_type(String id, Supplier<SensorType<?>> supplier){
        return SENSOR_TYPES.register(id, supplier);
    }
    
    public RegistryObject<Schedule> schedule(String id, Supplier<Schedule> supplier){
        return SCHEDULES.register(id, supplier);
    }
    
    public RegistryObject<Activity> activitie(String id, Supplier<Activity> supplier){
        return ACTIVITIES.register(id, supplier);
    }
    
    public RegistryObject<WorldCarver<?>> world_carver(String id, Supplier<WorldCarver<?>> supplier){
        return WORLD_CARVERS.register(id, supplier);
    }
    
    public RegistryObject<Feature<?>> feature(String id, Supplier<Feature<?>> supplier){
        return FEATURES.register(id, supplier);
    }
    
    public RegistryObject<ChunkStatus> chunk_statu(String id, Supplier<ChunkStatus> supplier){
        return CHUNK_STATUS.register(id, supplier);
    }
    
    public RegistryObject<BlockStateProviderType<?>> block_state_provider_type(String id, Supplier<BlockStateProviderType<?>> supplier){
        return BLOCK_STATE_PROVIDER_TYPES.register(id, supplier);
    }
    
    public RegistryObject<FoliagePlacerType<?>> foliage_placer_type(String id, Supplier<FoliagePlacerType<?>> supplier){
        return FOLIAGE_PLACER_TYPES.register(id, supplier);
    }
    
    public RegistryObject<TreeDecoratorType<?>> tree_decorator_type(String id, Supplier<TreeDecoratorType<?>> supplier){
        return TREE_DECORATOR_TYPES.register(id, supplier);
    }
    
    public RegistryObject<TrunkPlacerType<?>> trunk_placer_type(String id, Supplier<TrunkPlacerType<?>> supplier){
        return TRUNK_PLACER_TYPE.register(id, supplier);
    }
    
    public RegistryObject<Biome> biome(String id, Supplier<Biome> supplier){
        return BIOMES.register(id, supplier);
    }
}