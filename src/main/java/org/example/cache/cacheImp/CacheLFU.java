package org.example.cache.cacheImp;

import lombok.Setter;
import org.example.cache.Cacheable;
import org.example.util.ListLRU;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CacheLFU<T> implements Cacheable<T> {

    private final Map<String,T> hashMap = new HashMap<>();
    private final Map<String,Integer> hashMapLFU = new HashMap<>();
    private final ListLRU listLRU = new ListLRU();
    private final int capacity;

    private Consumer<T> consumer=null;



    public CacheLFU(int capacity) {
        this.capacity = capacity;
    }


    public Optional<T> get(T element){
        String hashOfElement = String.valueOf(element.hashCode());

        if(!hashMap.containsKey(hashOfElement)) {
            return Optional.empty();
        }

        hashMapLFU.put(hashOfElement,hashMapLFU.get(hashOfElement)+1);

        listLRU.put(hashOfElement);

        if(consumer!=null){
            consumer.accept(element);
        }
        return Optional.of(hashMap.get(hashOfElement));
    }
    public void put(T element){
        String hashOfElement = String.valueOf(element.hashCode());

        if(hashMap.size()==capacity){
            removeElement();
        }

        hashMapLFU.put(hashOfElement,1);
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

    private void removeElement(){
        Integer min =hashMapLFU.values().stream().sorted().findFirst().get();
        Set<Map.Entry<String,Integer>>  tmpSet = hashMapLFU.entrySet().stream()
                .filter(x-> Objects.equals(x.getValue(), min))
                .collect(Collectors.toSet());

        String hashLatestElement = listLRU.getHashLatestElement(tmpSet);
        hashMap.remove(hashLatestElement);
        hashMapLFU.remove(hashLatestElement);
    }

}
