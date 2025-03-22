package ru.blatfan.blatapi.common.registry;

import lombok.Getter;
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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import oshi.util.tuples.Pair;

import java.util.function.Supplier;

public class BlatRegister {
    public final String modid;
    public final DeferredRegister<Block> BLOCKS;
    public final DeferredRegister<Fluid> FLUIDS;
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
    
    public BlatRegister(String modid) {
        this.modid = modid;
        
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, modid);
        FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, modid);
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
    }
    
    public void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
        FLUIDS.register(eventBus);
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
    }
    
    public RegistryObject<Block> block(String id, Supplier<Block> supplier){
        BlatRegistryEvent<Block> e = new BlatRegistryEvent<>(this, ForgeRegistries.BLOCKS.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return BLOCKS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<Fluid> fluid(String id, Supplier<Fluid> supplier){
        BlatRegistryEvent<Fluid> e = new BlatRegistryEvent<>(this, ForgeRegistries.FLUIDS.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return FLUIDS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<Item> item(String id, Supplier<Item> supplier){
        BlatRegistryEvent<Item> e = new BlatRegistryEvent<>(this, ForgeRegistries.ITEMS.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return ITEMS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<CreativeModeTab> creative_mode_tab(String id, Supplier<CreativeModeTab> supplier){
        BlatRegistryEvent<CreativeModeTab> e = new BlatRegistryEvent<>(this, Registries.CREATIVE_MODE_TAB, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return CREATIVE_MODE_TAB.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<MobEffect> mob_effect(String id, Supplier<MobEffect> supplier){
        BlatRegistryEvent<MobEffect> e = new BlatRegistryEvent<>(this, ForgeRegistries.MOB_EFFECTS.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return MOB_EFFECTS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<SoundEvent> sound_event(String id, Supplier<SoundEvent> supplier){
        BlatRegistryEvent<SoundEvent> e = new BlatRegistryEvent<>(this, ForgeRegistries.SOUND_EVENTS.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return SOUND_EVENTS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<Potion> potion(String id, Supplier<Potion> supplier){
        BlatRegistryEvent<Potion> e = new BlatRegistryEvent<>(this, ForgeRegistries.POTIONS.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return POTIONS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<Enchantment> enchantment(String id, Supplier<Enchantment> supplier){
        BlatRegistryEvent<Enchantment> e = new BlatRegistryEvent<>(this, ForgeRegistries.ENCHANTMENTS.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return ENCHANTMENTS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public <T extends EntityType<?>> RegistryObject<T> entity_type(String id, Supplier<T> supplier){
        BlatRegistryEvent<EntityType<?>> e = new BlatRegistryEvent<>(this, ForgeRegistries.ENTITY_TYPES.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return (RegistryObject<T>)ENTITY_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public <T extends BlockEntityType<?>> RegistryObject<T> block_entity_type(String id, Supplier<T> supplier){
        BlatRegistryEvent<BlockEntityType<?>> e = new BlatRegistryEvent<>(this, ForgeRegistries.BLOCK_ENTITY_TYPES.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return (RegistryObject<T>)BLOCK_ENTITY_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public <T extends ParticleType<?>> RegistryObject<T> particle_type(String id, Supplier<T> supplier){
        BlatRegistryEvent<ParticleType<?>> e = new BlatRegistryEvent<>(this, ForgeRegistries.PARTICLE_TYPES.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return (RegistryObject<T>)PARTICLE_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public <T extends MenuType<?>> RegistryObject<T> menu_type(String id, Supplier<T> supplier){
        BlatRegistryEvent<MenuType<?>> e = new BlatRegistryEvent<>(this, ForgeRegistries.MENU_TYPES.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return (RegistryObject<T>)MENU_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<PaintingVariant> painting_variant(String id, Supplier<PaintingVariant> supplier){
        BlatRegistryEvent<PaintingVariant> e = new BlatRegistryEvent<>(this, ForgeRegistries.PAINTING_VARIANTS.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return PAINTING_VARIANTS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public <T extends RecipeType<?>> RegistryObject<T> recipe_type(String id, Supplier<T> supplier){
        BlatRegistryEvent<RecipeType<?>> e = new BlatRegistryEvent<>(this, ForgeRegistries.RECIPE_TYPES.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return (RegistryObject<T>) RECIPE_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public <T extends RecipeSerializer<?>> RegistryObject<T> recipe_serializer(String id, Supplier<T> supplier){
        BlatRegistryEvent<RecipeSerializer<?>> e = new BlatRegistryEvent<>(this, ForgeRegistries.RECIPE_SERIALIZERS.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return (RegistryObject<T>)RECIPE_SERIALIZERS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<Attribute> attribute(String id, Supplier<Attribute> supplier){
        BlatRegistryEvent<Attribute> e = new BlatRegistryEvent<>(this, ForgeRegistries.ATTRIBUTES.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return ATTRIBUTES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<StatType<?>> stat_type(String id, Supplier<StatType<?>> supplier){
        BlatRegistryEvent<StatType<?>> e = new BlatRegistryEvent<>(this, ForgeRegistries.STAT_TYPES.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return STAT_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<ArgumentTypeInfo<?,?>> command_argument_type(String id, Supplier<ArgumentTypeInfo<?,?>> supplier){
        BlatRegistryEvent<ArgumentTypeInfo<?,?>> e = new BlatRegistryEvent<>(this, ForgeRegistries.COMMAND_ARGUMENT_TYPES.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return COMMAND_ARGUMENT_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<VillagerProfession> villager_profession(String id, Supplier<VillagerProfession> supplier){
        BlatRegistryEvent<VillagerProfession> e = new BlatRegistryEvent<>(this, ForgeRegistries.VILLAGER_PROFESSIONS.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return VILLAGER_PROFESSIONS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<PoiType> poi_type(String id, Supplier<PoiType> supplier){
        BlatRegistryEvent<PoiType> e = new BlatRegistryEvent<>(this, ForgeRegistries.POI_TYPES.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return POI_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<MemoryModuleType<?>> memory_module_type(String id, Supplier<MemoryModuleType<?>> supplier){
        BlatRegistryEvent<MemoryModuleType<?>> e = new BlatRegistryEvent<>(this, ForgeRegistries.MEMORY_MODULE_TYPES.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return MEMORY_MODULE_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<SensorType<?>> sensor_type(String id, Supplier<SensorType<?>> supplier){
        BlatRegistryEvent<SensorType<?>> e = new BlatRegistryEvent<>(this, ForgeRegistries.SENSOR_TYPES.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return SENSOR_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<Schedule> schedule(String id, Supplier<Schedule> supplier){
        BlatRegistryEvent<Schedule> e = new BlatRegistryEvent<>(this, ForgeRegistries.SCHEDULES.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return SCHEDULES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<Activity> activitie(String id, Supplier<Activity> supplier){
        BlatRegistryEvent<Activity> e = new BlatRegistryEvent<>(this, ForgeRegistries.ACTIVITIES.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return ACTIVITIES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<WorldCarver<?>> world_carver(String id, Supplier<WorldCarver<?>> supplier){
        BlatRegistryEvent<WorldCarver<?>> e = new BlatRegistryEvent<>(this, ForgeRegistries.WORLD_CARVERS.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return WORLD_CARVERS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<Feature<?>> feature(String id, Supplier<Feature<?>> supplier){
        BlatRegistryEvent<Feature<?>> e = new BlatRegistryEvent<>(this, ForgeRegistries.FEATURES.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return FEATURES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<ChunkStatus> chunk_statu(String id, Supplier<ChunkStatus> supplier){
        BlatRegistryEvent<ChunkStatus> e = new BlatRegistryEvent<>(this, ForgeRegistries.CHUNK_STATUS.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return CHUNK_STATUS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<BlockStateProviderType<?>> block_state_provider_type(String id, Supplier<BlockStateProviderType<?>> supplier){
        BlatRegistryEvent<BlockStateProviderType<?>> e = new BlatRegistryEvent<>(this, ForgeRegistries.BLOCK_STATE_PROVIDER_TYPES.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return BLOCK_STATE_PROVIDER_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<FoliagePlacerType<?>> foliage_placer_type(String id, Supplier<FoliagePlacerType<?>> supplier){
        BlatRegistryEvent<FoliagePlacerType<?>> e = new BlatRegistryEvent<>(this, ForgeRegistries.FOLIAGE_PLACER_TYPES.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return FOLIAGE_PLACER_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<TreeDecoratorType<?>> tree_decorator_type(String id, Supplier<TreeDecoratorType<?>> supplier){
        BlatRegistryEvent<TreeDecoratorType<?>> e = new BlatRegistryEvent<>(this, ForgeRegistries.TREE_DECORATOR_TYPES.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return TREE_DECORATOR_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<Biome> biome(String id, Supplier<Biome> supplier){
        BlatRegistryEvent<Biome> e = new BlatRegistryEvent<>(this, ForgeRegistries.BIOMES.getRegistryKey(), new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return BIOMES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public <T extends Recipe<?>> RegistryObject<RecipeType<T>> recipe_type(String id){
        return recipe_type(id, ()-> RecipeType.simple(new ResourceLocation(modid, id)));
    }
    
    public RegistryObject<Block> block(String id){
        return block(id, ()-> new Block(BlockBehaviour.Properties.of()));
    }
    
    public RegistryObject<Item> item(String id){
        return item(id, ()-> new Item(new Item.Properties()));
    }
    
    public RegistryObject<SoundEvent> sound_event(String name){
        return sound_event(name, ()-> SoundEvent.createVariableRangeEvent(new ResourceLocation(modid, name)));
    }
    public  <T extends Entity> RegistryObject<EntityType<T>> entity_type(String name, MobCategory mobCategory, float width, float height, int trackingRange, EntityType.EntityFactory<T> factory) {
        return entity_type(name, ()->EntityType.Builder.of(factory, mobCategory)
            .setTrackingRange(trackingRange)
            .setUpdateInterval(1)
            .sized(width, height)
            .build(modid + ":" + name));
    }
}
