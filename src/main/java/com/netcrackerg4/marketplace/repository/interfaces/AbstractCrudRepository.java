package com.netcrackerg4.marketplace.repository.interfaces;


import com.netcrackerg4.marketplace.model.dto.product.ProductDto;

import java.util.Optional;

public interface AbstractCrudRepository<T,K> {

    void create(T item);
    Optional<ProductDto> read(K key);
    void update(T updItem);
    void delete(K key);
}
