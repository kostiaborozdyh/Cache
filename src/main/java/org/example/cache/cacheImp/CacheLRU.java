package org.example.cache.cacheImp;

import lombok.Setter;
import org.example.cache.Cacheable;
import org.example.util.ListLRU;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class CacheLRU<T> implements Cacheable<T> {

    private final Map<String,T> hashMap = new HashMap<>();

    private final ListLRU listLRU = new ListLRU();
    private final int capacity;

    private Consumer<T> consumer=null;
    @Override
    public Optional<T> get(T element) {
        String hashOfElement = String.valueOf(element.hashCode());

        if(!hashMap.containsKey(hashOfElement)) {
            return Optional.empty();
        }

        listLRU.put(hashOfElement);

        if(consumer!=null){
            consumer.accept(element);
        }
        return Optional.of(hashMap.get(hashOfElement));
    }
    @Override
    public void put(T element) {

        String hashOfElement = String.valueOf(element.hashCode());

        if(hashMap.size()==capacity){
            String hashLatestElement = listLRU.getHashLatestElement(hashMap.entrySet());
            hashMap.remove(hashLatestElement);
        }

        listLRU.put(hashOfElement);
        hashMap.put(hashOfElement,element);

        if(consumer!=null){
            consumer.accept(element);
        }
    }

    @Override
    public void setConsumer(Consumer<T> consumer) {
        this.consumer=consumer;
    }

    public CacheLRU(int capacity) {
        this.capacity = capacity;
    }
}
