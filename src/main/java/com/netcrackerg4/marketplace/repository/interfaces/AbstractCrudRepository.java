package com.netcrackerg4.marketplace.repository.interfaces;

import java.util.Optional;

public interface AbstractCrudRepository<T,K> {

    void create(T item);
    Optional<T> read(K key);
    void update(T updItem);
    void delete(K key);
}
