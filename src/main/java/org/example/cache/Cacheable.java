package org.example.cache;

import java.util.Optional;
import java.util.function.Consumer;

public interface Cacheable<T> {

      Optional<T> get(T element);
      void put(T element);

      void setConsumer(Consumer<T> consumer);

}
