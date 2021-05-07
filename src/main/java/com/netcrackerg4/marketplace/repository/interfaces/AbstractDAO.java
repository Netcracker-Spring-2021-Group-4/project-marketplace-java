package com.netcrackerg4.marketplace.repository.interfaces;

import java.util.Collection;
import java.util.Optional;

public interface AbstractDAO<T, K> {
    Optional<T> findByIdx(K idx);
    void create(T item);
}
