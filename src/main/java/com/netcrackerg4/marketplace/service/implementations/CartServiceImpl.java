package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.model.domain.AppProductEntity;
import com.netcrackerg4.marketplace.model.domain.CartItemEntity;
import com.netcrackerg4.marketplace.model.dto.product.CartItemDto;
import com.netcrackerg4.marketplace.repository.interfaces.ICartItemDao;
import com.netcrackerg4.marketplace.service.interfaces.ICartService;
import com.netcrackerg4.marketplace.service.interfaces.IProductService;
import com.netcrackerg4.marketplace.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

    private final ICartItemDao repository;
    private final IProductService productService;
    private final IUserService userService;

    @Override
    @Transactional
    public void addToCart(String email, CartItemDto item) {
        UUID productId = item.getProductId();
        AppProductEntity product = productService
                .findProductById(productId)
                .orElseThrow(() -> {
                    throw new IllegalStateException(String.format("Product with id %s not found",productId));
                });;
        if(product.getInStock() - product.getReserved() < item.getQuantity()) {
            throw new IllegalStateException(
                    String.format("There is only %d items in stock! Try buying less ;)", product.getInStock())
            );
        }
        CartItemEntity cartItemEntity =
                CartItemEntity.builder()
                .cartItemId(UUID.randomUUID())
                .customerId(userService.findByEmail(email).getUserId())
                .productId(productId)
                .quantity(item.getQuantity())
                .timestampAdded(new Timestamp(System.currentTimeMillis()))
                .build();

        repository.addToCart(cartItemEntity);
    }
}
