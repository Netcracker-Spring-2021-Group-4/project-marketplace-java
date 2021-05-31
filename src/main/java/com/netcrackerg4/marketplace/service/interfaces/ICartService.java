package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.dto.product.CartItemDto;

import java.util.UUID;

public interface ICartService {
    void addToCart(String email, CartItemDto item);
    void checkAvailability(UUID id, int quantity);
}
