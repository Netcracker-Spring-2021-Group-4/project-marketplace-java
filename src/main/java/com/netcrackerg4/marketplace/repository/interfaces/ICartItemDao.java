package com.netcrackerg4.marketplace.repository.interfaces;

import com.netcrackerg4.marketplace.model.dto.product.CartItemDto;

public interface ICartItemDao {

    void addToCard(CartItemDto item);
}
