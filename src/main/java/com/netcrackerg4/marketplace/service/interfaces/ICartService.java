package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.dto.product.CartItemDto;
import com.netcrackerg4.marketplace.model.dto.product.UpdateCartItemDto;
import com.netcrackerg4.marketplace.model.response.CartInfoResponse;

import java.util.List;
import java.util.UUID;

public interface ICartService {
    void addToCart(String email, CartItemDto item);
    void removeFromCart(String email, UpdateCartItemDto item);
    void checkAvailability(UUID id, int quantity);
    CartInfoResponse getCartInfoAuthorized(String email);
    CartInfoResponse getCartInfoNonAuthorized(List<CartItemDto> cartItems);
}
