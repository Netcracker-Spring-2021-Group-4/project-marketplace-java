package com.netcrackerg4.marketplace.repository.interfaces;

import com.netcrackerg4.marketplace.model.domain.CartItemEntity;
import com.netcrackerg4.marketplace.model.dto.product.CartItemDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICartItemDao {
    void addToCart(CartItemEntity item);
    Optional<CartItemEntity> getCartItemByProductAndCustomer(UUID customerId, UUID productId);
    void changeQuantityById(int quantity, UUID cartItemId);
    List<CartItemDto> getAuthCustomerCartItems(UUID id);
}
