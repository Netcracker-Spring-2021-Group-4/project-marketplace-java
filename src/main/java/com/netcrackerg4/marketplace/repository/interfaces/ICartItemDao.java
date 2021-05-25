package com.netcrackerg4.marketplace.repository.interfaces;

import com.netcrackerg4.marketplace.model.domain.CartItemEntity;

public interface ICartItemDao {

    void addToCart(CartItemEntity item);
}
