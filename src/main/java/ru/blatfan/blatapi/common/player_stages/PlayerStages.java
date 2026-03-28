package ru.blatfan.blatapi.common.player_stages;

import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.common.network.BlatApiPacketHandler;
import ru.blatfan.blatapi.utils.capacity.ICapacity;
import ru.blatfan.blatapi.utils.collection.Couple;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class PlayerStages implements ICapacity<PlayerStages> {
    public static final List<ResourceLocation> allStages = Collections.synchronizedList(new LinkedList<>());
    private final Map<ResourceLocation, Value<?>> DATA = new ConcurrentHashMap<>();
    
    private static <T> PlayerStageEvent.Add<T> sendAddEvent(Player player, ResourceLocation key, Value<T> value){
        var event = new PlayerStageEvent.Add<>(key, player, value);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }
    private static <T> PlayerStageEvent.Set<T> sendSetEvent(Player player, ResourceLocation key, Value<T> value){
        var event = new PlayerStageEvent.Set<>(key, player, value);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }
    private static PlayerStageEvent.Remove sendRemEvent(Player player, ResourceLocation key){
        var event = new PlayerStageEvent.Remove(player, key);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }
    
    private static boolean create(Player player, ResourceLocation key){
        if(allStages.contains(key)) return false;
        var event = new PlayerStageEvent.Create(player, key);
        if (event.isCanceled()) return true;
        allStages.add(event.getKey());
        return false;
    }
    
    @Override
    public void sync(Entity entity) {
        if(entity instanceof Player player)
            BlatApiPacketHandler.sendTo(player, new PlayerStagesSync(this));
    }
    
    public Map<ResourceLocation, Value<?>> getAll(){
        return Collections.unmodifiableMap(DATA);
    }
    
    public static PlayerStages get(Player player){
        return player.getCapability(PlayerStagesProvider.CAPABILITY, null).orElse(new PlayerStages());
    }
    
    public static boolean has(Player player, ResourceLocation key){
        return get(player).DATA.containsKey(key);
    }
    
    public static Object get(Player player, ResourceLocation key, Class<Object> clazz){
        PlayerStages stages = get(player);
        if(stages.DATA.get(key).isSimilar(clazz))
            return stages.DATA.get(key).getValue();
        return null;
    }
    public static void set(Player player, ResourceLocation key, Object value){
        if(create(player, key)) return;
        if(Value.getDes(value.getClass())==null) return;
        set(player, key, Value.getDes(value.getClass()).apply(value));
    }
    public static void set(Player player, ResourceLocation key, Value<?> value){
        if(create(player, key)) return;
        var event = sendSetEvent(player, key, value);
        if(event.isCanceled()) return;
        player.getCapability(PlayerStagesProvider.CAPABILITY, null).ifPresent(playerStages -> {
            playerStages.DATA.put(key, event.getValue());
            playerStages.sync(player);
        });
    }
    
    public static int getInt(Player player, ResourceLocation key){
        PlayerStages stages = get(player);
        if(stages.DATA.get(key) instanceof IntValue val)
            return val.getValue();
        return 0;
    }
    public static void setInt(Player player, ResourceLocation key, int value){
        IntValue val = new IntValue(value);
        set(player, key, val);
    }
    
    public static double getDouble(Player player, ResourceLocation key){
        PlayerStages stages = get(player);
        if(stages.DATA.get(key) instanceof DoubleValue val)
            return val.getValue();
        return 0;
    }
    public static void setDouble(Player player, ResourceLocation key, double value){
        DoubleValue val = new DoubleValue(value);
        set(player, key, val);
    }
    
    public static float getFloat(Player player, ResourceLocation key){
        PlayerStages stages = get(player);
        if(stages.DATA.get(key) instanceof FloatValue val)
            return val.getValue();
        return 0;
    }
    public static void setFloat(Player player, ResourceLocation key, float value){
        FloatValue val = new FloatValue(value);
        set(player, key, val);
    }
    
    public static String getString(Player player, ResourceLocation key){
        PlayerStages stages = get(player);
        if(stages.DATA.get(key) instanceof StringValue val)
            return val.getValue();
        return "";
    }
    public static void setString(Player player, ResourceLocation key, String value){
        StringValue val = new StringValue(value);
        set(player, key, val);
    }
    
    public static long getLong(Player player, ResourceLocation key){
        PlayerStages stages = get(player);
        if(stages.DATA.get(key) instanceof LongValue val)
            return val.getValue();
        return 0;
    }
    public static void setLong(Player player, ResourceLocation key, long value){
        LongValue val = new LongValue(value);
        set(player, key, val);
    }
    
    public static boolean getBool(Player player, ResourceLocation key){
        PlayerStages stages = get(player);
        if(stages.DATA.get(key) instanceof BooleanValue val)
            return val.getValue();
        return false;
    }
    public static void setBool(Player player, ResourceLocation key, boolean value){
        BooleanValue val = new BooleanValue(value);
        set(player, key, val);
    }
    
    public static ItemStack getItemStack(Player player, ResourceLocation key){
        PlayerStages stages = get(player);
        if(stages.DATA.get(key) instanceof ItemStackValue val)
            return val.getValue();
        return ItemStack.EMPTY;
    }
    public static void setItemStack(Player player, ResourceLocation key, ItemStack value){
        ItemStackValue val = new ItemStackValue(value);
        set(player, key, val);
    }
    
    public static void remove(Player player, ResourceLocation key){
        var event = sendRemEvent(player, key);
        if(!event.isCanceled())
            player.getCapability(PlayerStagesProvider.CAPABILITY, null).ifPresent(playerStages -> {
                playerStages.DATA.remove(event.getKey());
                playerStages.sync(player);
            });
    }
    
    @Override
    public Tag toNBT() {
        CompoundTag nbt = new CompoundTag();
        for(ResourceLocation key : DATA.keySet()) {
            Value<?> value = DATA.get(key);
            if(value==null) continue;
            CompoundTag vt = value.serializeNBT();
            vt.putString("type", value.getType().toString());
            nbt.put(key.toString(), vt);
        }
        return nbt;
    }
    
    @Override
    public void fromNBT(Tag tag) {
        DATA.clear();
        CompoundTag nbt = (CompoundTag) tag;
        for(String key : nbt.getAllKeys()){
            ResourceLocation stage = ResourceLocation.parse(key);
            CompoundTag nbtM = nbt.getCompound(key);
            ResourceLocation type = ResourceLocation.parse(nbtM.getString("type"));
            Value<?> value = Value.deserialize(type, nbtM);
            if(!allStages.contains(stage)) allStages.add(stage);
            if(value != null) DATA.put(stage, value);
        }
    }
    
    @Override
    public void copy(PlayerStages instance) {
        DATA.clear();
        for(ResourceLocation key : instance.DATA.keySet())
            DATA.put(key, instance.DATA.get(key));
    }
    
    @Override
    public PlayerStages clone() {
        PlayerStages instance = new PlayerStages();
        instance.copy(this);
        return instance;
    }
    
    @Getter
    public abstract static class Value<T> {
        private final T value;
        private final Class<T> clazz;
        private final ResourceLocation type;
        protected static final Map<Couple<ResourceLocation, Class<?>>, Function<Object, Value<?>>> TYPES = new LinkedHashMap<>();
        
        protected Value(ResourceLocation type, T value, Class<T> clazz) {
            this.value = value;
            this.type = type;
            this.clazz = clazz;
        }
        
        public static <V> void register(ResourceLocation id, Class<V> clazz, Function<Object, Value<?>> des){
            TYPES.put(new Couple<>(id, clazz), des);
        }
        
        public static void init() {
            register(BlatApi.loc("int"), Integer.class, PlayerStages.IntValue::des);
            register(BlatApi.loc("float"), Float.class, PlayerStages.FloatValue::des);
            register(BlatApi.loc("double"), Double.class, PlayerStages.DoubleValue::des);
            register(BlatApi.loc("boolean"), Boolean.class, PlayerStages.BooleanValue::des);
            register(BlatApi.loc("string"), String.class, PlayerStages.StringValue::des);
            register(BlatApi.loc("long"), Long.class, PlayerStages.LongValue::des);
            register(BlatApi.loc("item_stack"), ItemStack.class, PlayerStages.ItemStackValue::des);
        }
        
        public <V> boolean isSimilar(Class<V> oClazz){
            return clazz.equals(oClazz);
        }
        public boolean isSimilar(ResourceLocation oType){
            return type.equals(oType);
        }
        
        public abstract CompoundTag serializeNBT();
        public static Value<?> deserialize(ResourceLocation type, CompoundTag nbt){
            if(getDes(type)!=null)
                return getDes(type).apply(nbt);
            return null;
        }
        
        public static Map<Couple<ResourceLocation, Class<?>>, Function<Object, Value<?>>> types(){
            return Collections.unmodifiableMap(TYPES);
        }
        public static Function<Object, Value<?>> getDes(Object type){
            for(Couple<ResourceLocation, Class<?>> pr : types().keySet())
                if(
                    (type instanceof ResourceLocation rl && pr.getKey().equals(rl)) ||
                        (type instanceof Class<?> clazz && pr.getValue().equals(clazz))
                ) return types().get(pr);
            return null;
        }
    }
    
    public static class IntValue extends Value<Integer> {
        public IntValue(Integer value) {
            super(BlatApi.loc("int"), value, Integer.class);
        }
        
        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("value", getValue());
            return tag;
        }
        
        protected static Value<Integer> deserializeNBT(CompoundTag nbt) {
            return new IntValue(nbt.getInt("value"));
        }
        
        public static Value<Integer> des(Object value) {
            if(value instanceof Integer v)
                return new IntValue(v);
            if(value instanceof CompoundTag nbt)
                return deserializeNBT(nbt);
            return null;
        }
    }
    public static class ItemStackValue extends Value<ItemStack> {
        public ItemStackValue(ItemStack value) {
            super(BlatApi.loc("item_stack"), value, ItemStack.class);
        }
        
        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.put("value", getValue().serializeNBT());
            return tag;
        }
        
        protected static ItemStackValue deserializeNBT(CompoundTag nbt) {
            return new ItemStackValue(ItemStack.of(nbt.getCompound("value")));
        }
        
        public static ItemStackValue des(Object value) {
            if(value instanceof ItemStack v)
                return new ItemStackValue(v);
            if(value instanceof CompoundTag nbt)
                return deserializeNBT(nbt);
            return null;
        }
    }
    
    public static class DoubleValue extends Value<Double> {
        public DoubleValue(Double value) {
            super(BlatApi.loc("double"), value, Double.class);
        }
        
        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putDouble("value", getValue());
            return tag;
        }
        
        protected static DoubleValue deserializeNBT(CompoundTag nbt) {
            return new DoubleValue(nbt.getDouble("value"));
        }
        
        public static DoubleValue des(Object value) {
            if(value instanceof Double v)
                return new DoubleValue(v);
            if(value instanceof CompoundTag nbt)
                return deserializeNBT(nbt);
            return null;
        }
    }
    public static class FloatValue extends Value<Float> {
        public FloatValue(Float value) {
            super(BlatApi.loc("float"), value, Float.class);
        }
        
        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putFloat("value", getValue());
            return tag;
        }
        
        protected static FloatValue deserializeNBT(CompoundTag nbt) {
            return new FloatValue(nbt.getFloat("value"));
        }
        
        public static FloatValue des(Object value) {
            if(value instanceof Float v)
                return new FloatValue(v);
            if(value instanceof CompoundTag nbt)
                return deserializeNBT(nbt);
            return null;
        }
    }
    
    public static class StringValue extends Value<String> {
        public StringValue(String value) {
            super(BlatApi.loc("string"), value, String.class);
        }
        
        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putString("value", getValue());
            return tag;
        }
        
        protected static StringValue deserializeNBT(CompoundTag nbt) {
            return new StringValue(nbt.getString("value"));
        }
        
        public static StringValue des(Object value) {
            if(value instanceof String v)
                return new StringValue(v);
            if(value instanceof CompoundTag nbt)
                return deserializeNBT(nbt);
            return null;
        }
    }
    
    public static class LongValue extends Value<Long> {
        public LongValue(Long value) {
            super(BlatApi.loc("long"), value, Long.class);
        }
        
        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putLong("value", getValue());
            return tag;
        }
        
        protected static LongValue deserializeNBT(CompoundTag nbt) {
            return new LongValue(nbt.getLong("value"));
        }
        
        public static LongValue des(Object value) {
            if(value instanceof Long v)
                return new LongValue(v);
            if(value instanceof CompoundTag nbt)
                return deserializeNBT(nbt);
            return null;
        }
    }
    
    public static class BooleanValue extends Value<Boolean> {
        public BooleanValue(Boolean value) {
            super(BlatApi.loc("boolean"), value, Boolean.class);
        }
        
        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("value", getValue());
            return tag;
        }
        
        protected static BooleanValue deserializeNBT(CompoundTag nbt) {
            return new BooleanValue(nbt.getBoolean("value"));
        }
        
        public static BooleanValue des(Object value) {
            if(value instanceof Boolean v)
                return new BooleanValue(v);
            if(value instanceof CompoundTag nbt)
                return deserializeNBT(nbt);
            return null;
        }
    }
}
