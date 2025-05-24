package ru.blatfan.blatapi.utils;

import net.minecraft.resources.ResourceLocation;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FlexResourceLocation {
    private final List<Pair<Supplier<Boolean>, ResourceLocation>> data = new ArrayList<>();
    public void add(Supplier<Boolean> booleanConsumer, ResourceLocation resourceLocation){
        data.add(new Pair<>(booleanConsumer, resourceLocation));
    }
    public void remove(ResourceLocation resourceLocation){
        for(Pair<Supplier<Boolean>, ResourceLocation> pair : data){
            ResourceLocation rl = pair.getB();
            if(resourceLocation==rl) data.remove(pair);
        }
    }
    
    public ResourceLocation get(){
        for(Pair<Supplier<Boolean>, ResourceLocation> pair : data){
            if(pair.getA().get()) return pair.getB();
        }
        return null;
    }
}