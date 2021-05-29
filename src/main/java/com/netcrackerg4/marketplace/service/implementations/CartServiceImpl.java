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
    public static final String NOT_SO_MUCH_IN_STOCK = "There is only %d items in stock! Try buying less ;)";

    private final ICartItemDao cartItemDao;
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
                });
        int amountAvailable = product.getInStock() - product.getReserved();
        if( amountAvailable < item.getQuantity())
            throw new IllegalStateException(String.format(NOT_SO_MUCH_IN_STOCK, product.getInStock()));

        UUID customerId = userService.findByEmail(email).getUserId();
        var existingCartItem = cartItemDao.getCartItemByProductAndCustomer(customerId, productId);
        existingCartItem
                .ifPresentOrElse(
                    cartItemEntity -> {
                        UUID id = cartItemEntity.getCartItemId();
                        int addingQuantity = cartItemEntity.getQuantity() + item.getQuantity();
                        if(amountAvailable < addingQuantity)
                            throw new IllegalStateException(String.format(NOT_SO_MUCH_IN_STOCK, product.getInStock()));
                        else
                            cartItemDao.changeQuantityById(addingQuantity, id);
                    },
                    () -> {
                        CartItemEntity cartItemEntity =
                            CartItemEntity.builder()
                                .cartItemId(UUID.randomUUID())
                                .customerId(customerId)
                                .productId(productId)
                                .quantity(item.getQuantity())
                                .timestampAdded(new Timestamp(System.currentTimeMillis()))
                                .build();

                        cartItemDao.addToCart(cartItemEntity);
                    }
                );
    }
}
