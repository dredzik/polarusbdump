package io.typedef.polarusbdump.mapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pair<K, V> {

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    private K key;
    private V value;
}
