package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.dto.product.CartItemDto;

public interface ICartService {

    String addToCart(CartItemDto item);
}
