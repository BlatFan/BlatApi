package ru.blatfan.blatapi.common.registry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.registries.IForgeRegistry;
import oshi.util.tuples.Pair;

import java.util.function.Supplier;

@Getter@AllArgsConstructor
public class BlatRegistryEvent<T> extends Event {
    private final BlatRegister blatRegister;
    private final IForgeRegistry<T> registry;
    private Pair<String, Supplier<? extends T>> object;
    
    public void setSupplier(Supplier<? extends T> supplier){
        object = new Pair<>(object.getA(), supplier);
    }
    public void setId(String id){
        object = new Pair<>(id, object.getB());
    }
}