package org.example;

import org.apache.log4j.Logger;
import org.example.cache.Cacheable;
import org.example.cacheFactory.CacheFactory;
import org.example.cacheType.CacheType;
import org.example.entity.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class CacheTest {
    private static final Logger log = Logger.getLogger(CacheTest.class);

    @Test
    public void testInputCacheLFU(){
        Cacheable<User> cacheLFU = CacheFactory.createCacheStorage(CacheType.LFU,1);
        User testUser = new User(1,"Ivan");
        cacheLFU.put(testUser);
        assertThat(cacheLFU.get(testUser))
                .isEqualTo(Optional.of(testUser));
    }

    @Test
    public void testInputCacheLRU(){
        Cacheable<User> cacheLRU = CacheFactory.createCacheStorage(CacheType.LRU,1);
        User testUser = new User(1,"Ivan");
        cacheLRU.put(testUser);
        assertThat(cacheLRU.get(testUser))
                .isEqualTo(Optional.of(testUser));
    }



    @Test
    public void testSmallCacheLFU(){
        Cacheable<User> cacheLFU = CacheFactory.createCacheStorage(CacheType.LFU,2);
        User user1 = new User(1,"User1");
        User user2 = new User(2,"User2");

        cacheLFU.put(user1);
        cacheLFU.put(user2);

        cacheLFU.get(user1);

        User user3 = new User(3,"User3");
        cacheLFU.put(user3);

        assertThat(cacheLFU.get(user1))
                .isEqualTo(Optional.of(user1));
        assertThat(cacheLFU.get(user3))
                .isEqualTo(Optional.of(user3));
        assertThat(cacheLFU.get(user2))
                .isEqualTo(Optional.empty());
    }

    @Test
    public void testSmallCacheLRU(){
        Cacheable<User> cacheLRU = CacheFactory.createCacheStorage(CacheType.LRU,2);
        User user1 = new User(1,"User1");
        User user2 = new User(2,"User2");

        cacheLRU.put(user1);
        cacheLRU.put(user2);

        cacheLRU.get(user1);

        User user3 = new User(3,"User3");
        cacheLRU.put(user3);

        assertThat(cacheLRU.get(user1))
                .isEqualTo(Optional.of(user1));
        assertThat(cacheLRU.get(user3))
                .isEqualTo(Optional.of(user3));
        assertThat(cacheLRU.get(user2))
                .isEqualTo(Optional.empty());
    }

    @Test
    public void testBigCacheLFU(){
        Cacheable<User> cacheLFU = CacheFactory.createCacheStorage(CacheType.LFU,20);

        fillCache(cacheLFU);

        User user3 = new User(3,"User3");
        cacheLFU.get(user3);

        User user20 = new User(20,"User20");
        cacheLFU.put(user20);

        assertThat(cacheLFU.get(new User(0,"User0")))
                .isEqualTo(Optional.empty());
        assertThat(cacheLFU.get(user3))
                .isEqualTo(Optional.of(user3));
        assertThat(cacheLFU.get(user20))
                .isEqualTo(Optional.of(user20));
    }

    @Test
    public void testBigCacheLRU(){
        Cacheable<User> cacheLRU = CacheFactory.createCacheStorage(CacheType.LRU,20);

        fillCache(cacheLRU);

        User user0 = new User(0,"User0");
        cacheLRU.get(user0);

        User user20 = new User(20,"User20");
        cacheLRU.put(user20);

        assertThat(cacheLRU.get(user0))
                .isEqualTo(Optional.of(user0));
        assertThat(cacheLRU.get(new User(1,"User1")))
                .isEqualTo(Optional.empty());
        assertThat(cacheLRU.get(user20))
                .isEqualTo(Optional.of(user20));
    }


   @Test
    public void testConsumerCacheLRU(){

        Cacheable<User> cacheLRU = CacheFactory.createCacheStorage(CacheType.LRU,1);
        cacheLRU.setConsumer(log::info);
        User user0 = new User(0,"User0");
        cacheLRU.put(user0);
        cacheLRU.get(user0);
    }
    @Test
    public void testConsumerCacheLFU(){

        Cacheable<User> cacheLFU = CacheFactory.createCacheStorage(CacheType.LFU,1);
        cacheLFU.setConsumer(log::info);
        User user0 = new User(0,"User0");
        cacheLFU.put(user0);
        cacheLFU.get(user0);
    }

    private void fillCache(Cacheable<User> cacheable){
        for (int i = 0; i < 20; i++) {
            cacheable.put(new User(i,"User"+i));
        }
    }


}
