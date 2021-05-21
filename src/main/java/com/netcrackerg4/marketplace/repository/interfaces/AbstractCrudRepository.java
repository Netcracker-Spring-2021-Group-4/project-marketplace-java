package com.netcrackerg4.marketplace.repository.interfaces;


public interface AbstractCrudRepository<T,K> {

    void create(T item);
    T read(K key);
    void update(T updItem);
    void delete(K key);
}
