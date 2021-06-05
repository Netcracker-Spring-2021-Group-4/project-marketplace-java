package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.dto.product.CartItemDto;
import com.netcrackerg4.marketplace.model.response.CartInfoResponse;

import java.util.List;

public interface ICartService {
    boolean addToCart(String email, CartItemDto item);
    void addToCartListIfPossible(String email, List<CartItemDto> items);
    boolean removeFromCart(String email, CartItemDto item);
    CartInfoResponse getCartInfoAuthorized(String email);
    CartInfoResponse getCartInfoNonAuthorized(List<CartItemDto> cartItems);
    void makeCartReservation(List<CartItemDto> cartItems);
    void cancelCartReservation(List<CartItemDto> cartItems);
}
