package org.example.cacheFactory;

import org.example.cacheType.CacheType;
import org.example.cache.Cacheable;
import org.example.cache.cacheImp.CacheLFU;
import org.example.cache.cacheImp.CacheLRU;

public class CacheFactory {

    public static <T> Cacheable<T> createCacheStorage(CacheType cacheType, int capacity){
        Cacheable<T> cacheable = null;
        switch (cacheType){
            case LFU:
                cacheable = new CacheLFU<>(capacity);
                break;
            case LRU:
                cacheable = new CacheLRU<>(capacity);
                break;
        }
        return cacheable;
    }
}
