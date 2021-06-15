package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.dto.ContentErrorListWrapper;
import com.netcrackerg4.marketplace.model.dto.ContentErrorWrapper;
import com.netcrackerg4.marketplace.model.dto.product.CartItemDto;
import com.netcrackerg4.marketplace.model.response.CartInfoResponse;

import java.util.List;
import java.util.UUID;

public interface ICartService {
    ContentErrorWrapper<Integer> checkProductAvailability(UUID productId);
    ContentErrorWrapper<Boolean> addToCart(String email, CartItemDto item);
    List<String> addToCartList(String email, List<CartItemDto> items);
    boolean removeFromCart(String email, CartItemDto item);
    ContentErrorListWrapper<CartInfoResponse> getCartInfoAuthorized(String email);
    ContentErrorListWrapper<CartInfoResponse> getCartInfoNonAuthorized(List<CartItemDto> cartItems);
    void makeCartReservation(List<CartItemDto> cartItems);
    void cancelCartReservation(List<CartItemDto> cartItems);
    ContentErrorListWrapper<CartInfoResponse> getCartInfoByOrderId(UUID orderId);
}
