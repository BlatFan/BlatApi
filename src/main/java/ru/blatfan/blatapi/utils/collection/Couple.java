package ru.blatfan.blatapi.utils.collection;


import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter@Setter
public class Couple<K, V> {
    private K key;
    private V value;

    public Couple(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    @Override
    public String toString() {
        return key + "=" + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Couple) {
            Couple pair = (Couple) o;
            if (!Objects.equals(key, pair.key)) return false;
            return Objects.equals(value, pair.value);
        }
        return false;
    }

    public static <K,V> Couple<K,V> create(K key,V value){
        return new Couple<K,V>(key,value);
    }
}

