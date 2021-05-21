package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.model.dto.product.CartItemDto;
import com.netcrackerg4.marketplace.repository.impl.CartItemRepositoryImpl;
import com.netcrackerg4.marketplace.service.interfaces.ICartService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CartServiceImpl implements ICartService {

    private final CartItemRepositoryImpl repository;

    public CartServiceImpl(CartItemRepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public void addToCart(CartItemDto item) {
        //if product exists;
        //if quantity<=inStock

        item.setCartItemId(UUID.randomUUID());

       // repository.addToCard(item);
    }
}
