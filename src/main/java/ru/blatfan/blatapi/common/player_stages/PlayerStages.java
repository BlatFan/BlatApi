package ru.blatfan.blatapi.common.player_stages;

import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.fluffy_fur.common.network.FluffyFurPacketHandler;
import ru.blatfan.blatapi.utils.ICapacity;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class PlayerStages implements ICapacity<PlayerStages> {
    public static final List<String> allStages = new ArrayList<>();
    private final Map<String, Value<?>> DATA = new HashMap<>();
    
    private static <T> Value<T> sendSetEvent(Player player, String key, Value<T> value){
        PlayerStageEvent.Set<T> event = new PlayerStageEvent.Set<>(player, key, value);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getValue();
    }
    private static boolean sendRemEvent(Player player, String key){
        PlayerStageEvent.Remove event = new PlayerStageEvent.Remove(player, key);
        return !MinecraftForge.EVENT_BUS.post(event);
    }
    
    @Override
    public void sync(Entity entity) {
        if(entity instanceof Player player)
            FluffyFurPacketHandler.sendTo(player, new PlayerStagesSync(this));
    }
    
    public Map<String, Value<?>> getAll(){
        return Collections.unmodifiableMap(DATA);
    }
    
    public static PlayerStages get(Player player){
        return player.getCapability(PlayerStagesProvider.CAPABILITY, null).orElse(new PlayerStages());
    }
    
    public static boolean has(Player player, String key){
        return get(player).DATA.containsKey(key);
    }
    
    public static Object get(Player player, String key, Class<Object> clazz){
        PlayerStages stages = get(player);
        if(stages.DATA.get(key).isSimilar(clazz))
            return stages.DATA.get(key).getValue();
        return null;
    }
    public static void set(Player player, String key, Object value){
        if(!allStages.contains(key)) allStages.add(key);
        if(Value.getDes(value.getClass())==null) return;
        player.getCapability(PlayerStagesProvider.CAPABILITY, null).ifPresent(playerStages -> {
            playerStages.DATA.put(key, sendSetEvent(player, key, Value.getDes(value.getClass()).apply(value)));
            playerStages.sync(player);
        });
    }
    public static void set(Player player, String key, Value<?> value){
        if(!allStages.contains(key)) allStages.add(key);
        player.getCapability(PlayerStagesProvider.CAPABILITY, null).ifPresent(playerStages -> {
            playerStages.DATA.put(key, sendSetEvent(player, key, value));
            playerStages.sync(player);
        });
    }
    
    public static int getInt(Player player, String key){
        PlayerStages stages = get(player);
        if(stages.DATA.get(key) instanceof IntValue val)
            return val.getValue();
        return 0;
    }
    public static void setInt(Player player, String key, int value){
        if(!allStages.contains(key)) allStages.add(key);
        player.getCapability(PlayerStagesProvider.CAPABILITY, null).ifPresent(playerStages -> {
            playerStages.DATA.put(key, sendSetEvent(player, key, new IntValue(value)));
            playerStages.sync(player);
        });
    }
    
    public static double getDouble(Player player, String key){
        PlayerStages stages = get(player);
        if(stages.DATA.get(key) instanceof DoubleValue val)
            return val.getValue();
        return 0;
    }
    public static void setDouble(Player player, String key, double value){
        if(!allStages.contains(key)) allStages.add(key);
        player.getCapability(PlayerStagesProvider.CAPABILITY, null).ifPresent(playerStages -> {
            playerStages.DATA.put(key, sendSetEvent(player, key, new DoubleValue(value)));
            playerStages.sync(player);
        });
    }
    
    public static float getFloat(Player player, String key){
        PlayerStages stages = get(player);
        if(stages.DATA.get(key) instanceof FloatValue val)
            return val.getValue();
        return 0;
    }
    public static void setFloat(Player player, String key, float value){
        if(!allStages.contains(key)) allStages.add(key);
        player.getCapability(PlayerStagesProvider.CAPABILITY, null).ifPresent(playerStages -> {
            playerStages.DATA.put(key, sendSetEvent(player, key, new FloatValue(value)));
            playerStages.sync(player);
        });
    }
    
    public static String getString(Player player, String key){
        PlayerStages stages = get(player);
        if(stages.DATA.get(key) instanceof StringValue val)
            return val.getValue();
        return "";
    }
    public static void setString(Player player, String key, String value){
        if(!allStages.contains(key)) allStages.add(key);
        player.getCapability(PlayerStagesProvider.CAPABILITY, null).ifPresent(playerStages -> {
            playerStages.DATA.put(key, sendSetEvent(player, key, new StringValue(value)));
            playerStages.sync(player);
        });
    }
    
    public static long getLong(Player player, String key){
        PlayerStages stages = get(player);
        if(stages.DATA.get(key) instanceof LongValue val)
            return val.getValue();
        return 0;
    }
    public static void setLong(Player player, String key, long value){
        if(!allStages.contains(key)) allStages.add(key);
        player.getCapability(PlayerStagesProvider.CAPABILITY, null).ifPresent(playerStages -> {
            playerStages.DATA.put(key, sendSetEvent(player, key, new LongValue(value)));
            playerStages.sync(player);
        });
    }
    
    public static boolean getBool(Player player, String key){
        PlayerStages stages = get(player);
        if(stages.DATA.get(key) instanceof BooleanValue val)
            return val.getValue();
        return false;
    }
    public static void setBool(Player player, String key, boolean value){
        if(!allStages.contains(key)) allStages.add(key);
        player.getCapability(PlayerStagesProvider.CAPABILITY, null).ifPresent(playerStages -> {
            playerStages.DATA.put(key, sendSetEvent(player, key, new BooleanValue(value)));
            playerStages.sync(player);
        });
    }
    
    public static CompoundTag getCompoundTag(Player player, String key){
        PlayerStages stages = get(player);
        if(stages.DATA.get(key) instanceof CompoundTagValue val)
            return val.getValue();
        return null;
    }
    public static void setCompoundTag(Player player, String key, CompoundTag value){
        if(!allStages.contains(key)) allStages.add(key);
        player.getCapability(PlayerStagesProvider.CAPABILITY, null).ifPresent(playerStages -> {
            playerStages.DATA.put(key, sendSetEvent(player, key, new CompoundTagValue(value)));
            playerStages.sync(player);
        });
    }
    
    public static void remove(Player player, String key){
        if(sendRemEvent(player, key))
            player.getCapability(PlayerStagesProvider.CAPABILITY, null).ifPresent(playerStages -> {
                playerStages.DATA.remove(key);
                playerStages.sync(player);
            });
    }
    
    @Override
    public Tag toNBT() {
        CompoundTag nbt = new CompoundTag();
        for(String key : DATA.keySet()) {
            Value<?> value = DATA.get(key);
            if(value==null) continue;
            CompoundTag vt = value.serializeNBT();
            vt.putString("type", value.getType().toString());
            nbt.put(key, vt);
        }
        return nbt;
    }
    
    @Override
    public void fromNBT(Tag tag) {
        DATA.clear();
        CompoundTag nbt = (CompoundTag) tag;
        for(String key : nbt.getAllKeys()){
            CompoundTag nbtM = nbt.getCompound(key);
            ResourceLocation type = ResourceLocation.tryParse(nbtM.getString("type"));
            DATA.put(key, Value.deserialize(type, nbtM));
        }
    }
    
    @Override
    public void copy(PlayerStages instance) {
        DATA.clear();
        for(String key : instance.DATA.keySet())
            DATA.put(key, instance.DATA.get(key));
    }
    
    @Override
    public PlayerStages clone() {
        PlayerStages instance = new PlayerStages();
        instance.copy(this);
        return instance;
    }
    
    @Getter
    public abstract static class Value<T> implements Predicate<Object>, Function<Object, Value<?>> {
        private final T value;
        private final Class<T> clazz;
        private final ResourceLocation type;
        private static final Map<Predicate<Object>, Function<Object, Value<?>>> TYPES = new LinkedHashMap<>();
        
        protected Value(ResourceLocation type, T value, Class<T> clazz) {
            this.value = value;
            this.type = type;
            this.clazz = clazz;
            TYPES.put(this, this);
        }
        
        @Override
        public Value<T> apply(Object o) {
            if(o instanceof CompoundTag tag) return deserializeNBT(tag);
            return fromValue(o);
        }
        
        @Override
        public boolean test(Object o) {
            if(o instanceof ResourceLocation rl) return isSimilar(rl);
            if(o instanceof Class<?> tClass) return isSimilar(tClass);
            return false;
        }
        
        public <V> boolean isSimilar(Class<V> oClazz){
            return clazz.equals(oClazz);
        }
        public boolean isSimilar(ResourceLocation oType){
            return type.equals(oType);
        }
        
        public abstract CompoundTag serializeNBT();
        protected abstract Value<T> deserializeNBT(CompoundTag nbt);
        protected abstract Value<T> fromValue(Object value);
        public static Value<?> deserialize(ResourceLocation type, CompoundTag nbt){
            if(getDes(type)!=null)
                return getDes(type).apply(nbt);
            return null;
        }
        
        public static Map<Predicate<Object>, Function<Object, Value<?>>> types(){
            return Collections.unmodifiableMap(TYPES);
        }
        public static Function<Object, Value<?>> getDes(Object type){
            for(Predicate<Object> pr : types().keySet())
                if(pr.test(type))
                    return types().get(pr);
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
        
        @Override
        protected Value<Integer> deserializeNBT(CompoundTag nbt) {
            return new IntValue(nbt.getInt("value"));
        }
        
        @Override
        protected Value<Integer> fromValue(Object value) {
            if(value instanceof Integer v)
                return new IntValue(v);
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
        
        @Override
        protected Value<Double> deserializeNBT(CompoundTag nbt) {
            return new DoubleValue(nbt.getDouble("value"));
        }
        
        @Override
        protected Value<Double> fromValue(Object value) {
            if(value instanceof Double v)
                return new DoubleValue(v);
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
        
        @Override
        protected Value<Float> deserializeNBT(CompoundTag nbt) {
            return new FloatValue(nbt.getFloat("value"));
        }
        
        @Override
        protected Value<Float> fromValue(Object value) {
            if(value instanceof Float v)
                return new FloatValue(v);
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
        
        @Override
        protected Value<String> deserializeNBT(CompoundTag nbt) {
            return new StringValue(nbt.getString("value"));
        }
        
        @Override
        protected Value<String> fromValue(Object value) {
            if(value instanceof String v)
                return new StringValue(v);
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
        
        @Override
        protected Value<Long> deserializeNBT(CompoundTag nbt) {
            return new LongValue(nbt.getLong("value"));
        }
        
        @Override
        protected Value<Long> fromValue(Object value) {
            if(value instanceof Long v)
                return new LongValue(v);
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
        
        @Override
        protected Value<Boolean> deserializeNBT(CompoundTag nbt) {
            return new BooleanValue(nbt.getBoolean("value"));
        }
        
        @Override
        protected Value<Boolean> fromValue(Object value) {
            if(value instanceof Boolean v)
                return new BooleanValue(v);
            return null;
        }
    }
    
    public static class CompoundTagValue extends Value<CompoundTag> {
        public CompoundTagValue(CompoundTag value) {
            super(BlatApi.loc("compound_tag"), value, CompoundTag.class);
        }
        
        @Override
        public CompoundTag serializeNBT() {
            return getValue();
        }
        
        @Override
        protected Value<CompoundTag> deserializeNBT(CompoundTag nbt) {
            return new CompoundTagValue(nbt);
        }
        
        @Override
        protected Value<CompoundTag> fromValue(Object value) {
            if(value instanceof CompoundTag v)
                return new CompoundTagValue(v);
            return null;
        }
    }
}
