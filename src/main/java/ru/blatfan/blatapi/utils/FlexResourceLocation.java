package ru.blatfan.blatapi.utils;

import net.minecraft.resources.ResourceLocation;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("ALL")
public class FlexResourceLocation {
    private final List<Pair<Supplier<Boolean>, ResourceLocation>> data = new ArrayList<>();
    private final ResourceLocation defaultRL;
    
    public FlexResourceLocation(ResourceLocation defaultRL) {
        this.defaultRL = defaultRL;
    }
    
    public FlexResourceLocation add(Supplier<Boolean> booleanConsumer, ResourceLocation resourceLocation){
        data.add(new Pair<>(booleanConsumer, resourceLocation));
        return this;
    }
    public FlexResourceLocation remove(ResourceLocation resourceLocation){
        for(Pair<Supplier<Boolean>, ResourceLocation> pair : data){
            ResourceLocation rl = pair.getB();
            if(resourceLocation==rl) data.remove(pair);
        }
        return this;
    }
    
    public ResourceLocation get(){
        for(Pair<Supplier<Boolean>, ResourceLocation> pair : data)
            if(pair.getA().get()) return pair.getB();
        return defaultRL;
    }
    
    public String getNamespace() {
        return get().getNamespace();
    }
    
    public String getPath() {
        return get().getPath();
    }
    
    @Override
    public boolean equals(Object pOther) {
        if (this == pOther)
            return true;
        else if (!(pOther instanceof FlexResourceLocation))
            if(pOther instanceof ResourceLocation rlO)
                return defaultRL.equals(rlO);
            else return false;
        else {
            FlexResourceLocation resourcelocation = (FlexResourceLocation)pOther;
            return get().equals(resourcelocation.getNamespace()) && get().equals(resourcelocation.getPath());
        }
    }
}