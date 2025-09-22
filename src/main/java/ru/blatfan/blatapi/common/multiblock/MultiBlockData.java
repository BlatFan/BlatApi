package ru.blatfan.blatapi.common.multiblock;

import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.api.multiblock.StateMatcher;
import ru.blatfan.blatapi.api.multiblock.TriPredicate;
import ru.blatfan.blatapi.common.multiblock.matcher.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class MultiBlockData {
    private static final Map<ResourceLocation, Function<JsonObject, StateMatcher>> stateMatcherJsonLoaders = new ConcurrentHashMap<>();
    public static void registerStateMatcherLoader(ResourceLocation id, Function<JsonObject, StateMatcher> jsonLoader) {
        stateMatcherJsonLoaders.put(id, jsonLoader);
    }
    public static Function<JsonObject, StateMatcher> getStateMatcherJsonLoader(ResourceLocation id) {
        var loader = stateMatcherJsonLoaders.get(id);
        if (loader == null) {
            throw new IllegalArgumentException("No json loader registered for state matcher type " + id);
        }
        return loader;
    }
    
    private static final Map<ResourceLocation, TriPredicate<BlockGetter, BlockPos, BlockState>> predicates = new ConcurrentHashMap<>();
    public static void registerPredicate(ResourceLocation id, TriPredicate<BlockGetter, BlockPos, BlockState> predicate) {
        predicates.put(id, predicate);
    }
    public static TriPredicate<BlockGetter, BlockPos, BlockState> getPredicate(ResourceLocation id) {
        var predicate = predicates.get(id);
        if (predicate == null) {
            throw new IllegalArgumentException("No predicated registered for id " + id);
        }
        return predicate;
    }
    
    public static void init(){
        registerStateMatcherLoader(AnyMatcher.TYPE, AnyMatcher::fromJson);
        registerStateMatcherLoader(BlockMatcher.TYPE, BlockMatcher::fromJson);
        registerStateMatcherLoader(BlockStateMatcher.TYPE, BlockStateMatcher::fromJson);
        registerStateMatcherLoader(BlockStatePropertyMatcher.TYPE, BlockStatePropertyMatcher::fromJson);
        registerStateMatcherLoader(DisplayOnlyMatcher.TYPE, DisplayOnlyMatcher::fromJson);
        registerStateMatcherLoader(PredicateMatcher.TYPE, PredicateMatcher::fromJson);
        registerStateMatcherLoader(TagMatcher.TYPE, TagMatcher::fromJson);
        
        registerPredicate(Matchers.AIR.getPredicateId(), (getter, pos, state) -> state.isAir());
        registerPredicate(BlatApi.loc("non_solid"), (getter, pos, state) -> !state.isSolid());
    }
}