package com.netcrackerg4.marketplace.repository.interfaces;

import com.netcrackerg4.marketplace.model.domain.AppUserEntity;

import java.util.Optional;

public interface AbstractDAO<T, K> {
    Optional<T> findByEmail(K idx);

    void create(T item);

    T read(K key);

    void update(T updItem);

    void delete(K key);
}
