package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.model.domain.CartItemEntity;
import com.netcrackerg4.marketplace.model.domain.ProductEntity;
import com.netcrackerg4.marketplace.model.dto.product.CartItemDto;
import com.netcrackerg4.marketplace.model.response.CartInfoResponse;
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
    public CartInfoResponse getCartInfoAuthorized(String email) {
        return null;
    }

    @Override
    public void checkAvailability(UUID id, int quantity) {
        getAmountAvailable(id,quantity);
    }

    @Override
    @Transactional
    public void addToCart(String email, CartItemDto item) {
        UUID productId = item.getProductId();
        int amountAvailable = getAmountAvailable(item.getProductId(), item.getQuantity());

        UUID customerId = userService.findByEmail(email).getUserId();
        var existingCartItem = cartItemDao.getCartItemByProductAndCustomer(customerId, productId);
        existingCartItem
                .ifPresentOrElse(
                    cartItemEntity -> {
                        UUID id = cartItemEntity.getCartItemId();
                        int addingQuantity = cartItemEntity.getQuantity() + item.getQuantity();
                        if(amountAvailable < addingQuantity)
                            throw new IllegalStateException(String.format(NOT_SO_MUCH_IN_STOCK, amountAvailable));
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

    private int getAmountAvailable(UUID id, int quantity) {
        ProductEntity product = productService
                .findProductById(id)
                .orElseThrow(() -> {
                    throw new IllegalStateException(String.format("Product with id %s not found", id));
                });
        if(!product.getIsActive()) throw new IllegalStateException("Product is not available now");
        int amountAvailable = product.getInStock() - product.getReserved();
        if( amountAvailable < quantity)
            throw new IllegalStateException(String.format(NOT_SO_MUCH_IN_STOCK, amountAvailable));
        return amountAvailable;
    }
}
