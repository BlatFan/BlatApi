package ru.blatfan.blatapi.common.registry;

import lombok.Getter;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.inventory.MenuType;
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

@Getter
public class BlatRegister {
    private final String modid;
    private final DeferredRegister<Block> BLOCKS;
    private final DeferredRegister<Fluid> FLUIDS;
    private final DeferredRegister<Item> ITEMS;
    private final DeferredRegister<MobEffect> MOB_EFFECTS;
    private final DeferredRegister<SoundEvent> SOUND_EVENTS;
    private final DeferredRegister<Potion> POTIONS;
    private final DeferredRegister<Enchantment> ENCHANTMENTS;
    private final DeferredRegister<EntityType<?>> ENTITY_TYPES;
    private final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES;
    private final DeferredRegister<ParticleType<?>> PARTICLE_TYPES;
    private final DeferredRegister<MenuType<?>> MENU_TYPES;
    private final DeferredRegister<PaintingVariant> PAINTING_VARIANTS;
    private final DeferredRegister<RecipeType<?>> RECIPE_TYPES;
    private final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS;
    private final DeferredRegister<Attribute> ATTRIBUTES;
    private final DeferredRegister<StatType<?>> STAT_TYPES;
    private final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES;
    private final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS;
    private final DeferredRegister<PoiType> POI_TYPES;
    private final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES;
    private final DeferredRegister<SensorType<?>> SENSOR_TYPES;
    private final DeferredRegister<Schedule> SCHEDULES;
    private final DeferredRegister<Activity> ACTIVITIES;
    private final DeferredRegister<WorldCarver<?>> WORLD_CARVERS;
    private final DeferredRegister<Feature<?>> FEATURES;
    private final DeferredRegister<ChunkStatus> CHUNK_STATUS;
    private final DeferredRegister<BlockStateProviderType<?>> BLOCK_STATE_PROVIDER_TYPES;
    private final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPES;
    private final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATOR_TYPES;
    private final DeferredRegister<Biome> BIOMES;
    
    
    public BlatRegister(String modid) {
        this.modid = modid;
        
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, modid);
        FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, modid);
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, modid);
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
        BlatRegistryEvent<Block> e = new BlatRegistryEvent<Block>(this, ForgeRegistries.BLOCKS, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return BLOCKS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<Fluid> fluid(String id, Supplier<Fluid> supplier){
        BlatRegistryEvent<Fluid> e = new BlatRegistryEvent<Fluid>(this, ForgeRegistries.FLUIDS, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return FLUIDS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<Item> item(String id, Supplier<Item> supplier){
        BlatRegistryEvent<Item> e = new BlatRegistryEvent<Item>(this, ForgeRegistries.ITEMS, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return ITEMS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<MobEffect> mob_effect(String id, Supplier<MobEffect> supplier){
        BlatRegistryEvent<MobEffect> e = new BlatRegistryEvent<MobEffect>(this, ForgeRegistries.MOB_EFFECTS, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return MOB_EFFECTS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<SoundEvent> sound_event(String id, Supplier<SoundEvent> supplier){
        BlatRegistryEvent<SoundEvent> e = new BlatRegistryEvent<SoundEvent>(this, ForgeRegistries.SOUND_EVENTS, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return SOUND_EVENTS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<Potion> potion(String id, Supplier<Potion> supplier){
        BlatRegistryEvent<Potion> e = new BlatRegistryEvent<Potion>(this, ForgeRegistries.POTIONS, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return POTIONS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<Enchantment> enchantment(String id, Supplier<Enchantment> supplier){
        BlatRegistryEvent<Enchantment> e = new BlatRegistryEvent<Enchantment>(this, ForgeRegistries.ENCHANTMENTS, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return ENCHANTMENTS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public <T extends EntityType<?>> RegistryObject<T> entity_type(String id, Supplier<T> supplier){
        BlatRegistryEvent<EntityType<?>> e = new BlatRegistryEvent<EntityType<?>>(this, ForgeRegistries.ENTITY_TYPES, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return (RegistryObject<T>)ENTITY_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public <T extends BlockEntityType<?>> RegistryObject<T> block_entity_type(String id, Supplier<T> supplier){
        BlatRegistryEvent<BlockEntityType<?>> e = new BlatRegistryEvent<BlockEntityType<?>>(this, ForgeRegistries.BLOCK_ENTITY_TYPES, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return (RegistryObject<T>)BLOCK_ENTITY_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public <T extends ParticleType<?>> RegistryObject<T> particle_type(String id, Supplier<T> supplier){
        BlatRegistryEvent<ParticleType<?>> e = new BlatRegistryEvent<ParticleType<?>>(this, ForgeRegistries.PARTICLE_TYPES, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return (RegistryObject<T>)PARTICLE_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public <T extends MenuType<?>> RegistryObject<T> menu_type(String id, Supplier<T> supplier){
        BlatRegistryEvent<MenuType<?>> e = new BlatRegistryEvent<>(this, ForgeRegistries.MENU_TYPES, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return (RegistryObject<T>)MENU_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<PaintingVariant> painting_variant(String id, Supplier<PaintingVariant> supplier){
        BlatRegistryEvent<PaintingVariant> e = new BlatRegistryEvent<PaintingVariant>(this, ForgeRegistries.PAINTING_VARIANTS, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return PAINTING_VARIANTS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public <T extends RecipeType<?>> RegistryObject<T> recipe_type(String id, Supplier<T> supplier){
        BlatRegistryEvent<RecipeType<?>> e = new BlatRegistryEvent<>(this, ForgeRegistries.RECIPE_TYPES, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return (RegistryObject<T>) RECIPE_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public <T extends RecipeSerializer<?>> RegistryObject<T> recipe_serializer(String id, Supplier<T> supplier){
        BlatRegistryEvent<RecipeSerializer<?>> e = new BlatRegistryEvent<RecipeSerializer<?>>(this, ForgeRegistries.RECIPE_SERIALIZERS, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return (RegistryObject<T>)RECIPE_SERIALIZERS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<Attribute> attribute(String id, Supplier<Attribute> supplier){
        BlatRegistryEvent<Attribute> e = new BlatRegistryEvent<Attribute>(this, ForgeRegistries.ATTRIBUTES, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return ATTRIBUTES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<StatType<?>> stat_type(String id, Supplier<StatType<?>> supplier){
        BlatRegistryEvent<StatType<?>> e = new BlatRegistryEvent<StatType<?>>(this, ForgeRegistries.STAT_TYPES, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return STAT_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<ArgumentTypeInfo<?,?>> command_argument_type(String id, Supplier<ArgumentTypeInfo<?,?>> supplier){
        BlatRegistryEvent<ArgumentTypeInfo<?,?>> e = new BlatRegistryEvent<ArgumentTypeInfo<?,?>>(this, ForgeRegistries.COMMAND_ARGUMENT_TYPES, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return COMMAND_ARGUMENT_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<VillagerProfession> villager_profession(String id, Supplier<VillagerProfession> supplier){
        BlatRegistryEvent<VillagerProfession> e = new BlatRegistryEvent<VillagerProfession>(this, ForgeRegistries.VILLAGER_PROFESSIONS, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return VILLAGER_PROFESSIONS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<PoiType> poi_type(String id, Supplier<PoiType> supplier){
        BlatRegistryEvent<PoiType> e = new BlatRegistryEvent<PoiType>(this, ForgeRegistries.POI_TYPES, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return POI_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<MemoryModuleType<?>> memory_module_type(String id, Supplier<MemoryModuleType<?>> supplier){
        BlatRegistryEvent<MemoryModuleType<?>> e = new BlatRegistryEvent<MemoryModuleType<?>>(this, ForgeRegistries.MEMORY_MODULE_TYPES, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return MEMORY_MODULE_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<SensorType<?>> sensor_type(String id, Supplier<SensorType<?>> supplier){
        BlatRegistryEvent<SensorType<?>> e = new BlatRegistryEvent<SensorType<?>>(this, ForgeRegistries.SENSOR_TYPES, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return SENSOR_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<Schedule> schedule(String id, Supplier<Schedule> supplier){
        BlatRegistryEvent<Schedule> e = new BlatRegistryEvent<Schedule>(this, ForgeRegistries.SCHEDULES, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return SCHEDULES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<Activity> activitie(String id, Supplier<Activity> supplier){
        BlatRegistryEvent<Activity> e = new BlatRegistryEvent<Activity>(this, ForgeRegistries.ACTIVITIES, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return ACTIVITIES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<WorldCarver<?>> world_carver(String id, Supplier<WorldCarver<?>> supplier){
        BlatRegistryEvent<WorldCarver<?>> e = new BlatRegistryEvent<WorldCarver<?>>(this, ForgeRegistries.WORLD_CARVERS, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return WORLD_CARVERS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<Feature<?>> feature(String id, Supplier<Feature<?>> supplier){
        BlatRegistryEvent<Feature<?>> e = new BlatRegistryEvent<Feature<?>>(this, ForgeRegistries.FEATURES, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return FEATURES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<ChunkStatus> chunk_statu(String id, Supplier<ChunkStatus> supplier){
        BlatRegistryEvent<ChunkStatus> e = new BlatRegistryEvent<ChunkStatus>(this, ForgeRegistries.CHUNK_STATUS, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return CHUNK_STATUS.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<BlockStateProviderType<?>> block_state_provider_type(String id, Supplier<BlockStateProviderType<?>> supplier){
        BlatRegistryEvent<BlockStateProviderType<?>> e = new BlatRegistryEvent<BlockStateProviderType<?>>(this, ForgeRegistries.BLOCK_STATE_PROVIDER_TYPES, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return BLOCK_STATE_PROVIDER_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<FoliagePlacerType<?>> foliage_placer_type(String id, Supplier<FoliagePlacerType<?>> supplier){
        BlatRegistryEvent<FoliagePlacerType<?>> e = new BlatRegistryEvent<FoliagePlacerType<?>>(this, ForgeRegistries.FOLIAGE_PLACER_TYPES, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return FOLIAGE_PLACER_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<TreeDecoratorType<?>> tree_decorator_type(String id, Supplier<TreeDecoratorType<?>> supplier){
        BlatRegistryEvent<TreeDecoratorType<?>> e = new BlatRegistryEvent<TreeDecoratorType<?>>(this, ForgeRegistries.TREE_DECORATOR_TYPES, new Pair<>(id, supplier));
        MinecraftForge.EVENT_BUS.post(e);
        return TREE_DECORATOR_TYPES.register(e.getObject().getA(), e.getObject().getB());
    }
    
    public RegistryObject<Biome> biome(String id, Supplier<Biome> supplier){
        BlatRegistryEvent<Biome> e = new BlatRegistryEvent<Biome>(this, ForgeRegistries.BIOMES, new Pair<>(id, supplier));
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
}
