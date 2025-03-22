package ru.blatfan.blatapi.common.registry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.Event;
import oshi.util.tuples.Pair;

import java.util.function.Supplier;

@Getter@AllArgsConstructor
public class BlatRegistryEvent<T> extends Event {
    private final BlatRegister blatRegister;
    private final ResourceKey<Registry<T>> registry;
    private Pair<String, Supplier<? extends T>> object;
    
    public void setSupplier(Supplier<? extends T> supplier){
        object = new Pair<>(object.getA(), supplier);
    }
    public void setId(String id){
        object = new Pair<>(id, object.getB());
    }
}