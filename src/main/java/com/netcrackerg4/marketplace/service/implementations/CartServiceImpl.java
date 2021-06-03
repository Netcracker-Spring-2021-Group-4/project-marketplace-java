package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.model.domain.CartItemEntity;
import com.netcrackerg4.marketplace.model.domain.ProductEntity;
import com.netcrackerg4.marketplace.model.dto.product.CartItemDto;
import com.netcrackerg4.marketplace.model.response.CartInfoResponse;
import com.netcrackerg4.marketplace.model.response.CartProductInfo;
import com.netcrackerg4.marketplace.repository.interfaces.ICartItemDao;
import com.netcrackerg4.marketplace.service.interfaces.ICartService;
import com.netcrackerg4.marketplace.service.interfaces.ICategoryService;
import com.netcrackerg4.marketplace.service.interfaces.IProductService;
import com.netcrackerg4.marketplace.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {
    public static final String NOT_SO_MUCH_IN_STOCK = "There is only %d items in stock! Try buying less ;)";

    private final ICartItemDao cartItemDao;
    private final IProductService productService;
    private final ICategoryService categoryService;
    private final IUserService userService;

    @Override
    @Transactional
    public void makeCartReservation(List<CartItemDto> cartItems) {
        cartItems.stream().filter( i -> {
            var product = checkForExistenceAndReturn(i.getProductId());
            checkIfProductIsActive(product);
            return product.getInStock() < i.getQuantity() + product.getReserved();
        }).findFirst().ifPresentOrElse(
            i ->{
                throw new IllegalStateException(
                        String.format("You cannot reserve %d items of product with id %s",
                                i.getQuantity(), i.getProductId()));
            },
            () -> cartItems.forEach(i -> cartItemDao.reserveProduct(i.getQuantity(), i.getProductId()))
        );

    }

    @Override
    @Transactional
    public void cancelCartReservation(List<CartItemDto> cartItems) {
        cartItems.stream().filter(i -> {
            var product = checkForExistenceAndReturn(i.getProductId());
            checkIfProductIsActive(product);
            return product.getReserved() - i.getQuantity() < 0;
        }).findFirst().ifPresentOrElse(
            i -> {
                throw new IllegalStateException(String.format("Too much cancelling reservations for product with id %s", i.getProductId()));
            },
            () -> cartItems.forEach(i -> cartItemDao.cancelReservation(i.getQuantity(), i.getProductId()))
        );
    }

    @Override
    public CartInfoResponse getCartInfoAuthorized(String email) {
        UUID userId = userService.findByEmail(email).getUserId();
        List<CartItemDto> cartItems = cartItemDao.getAuthCustomerCartItems(userId);
        return getCartInfoResponse(cartItems);
    }

    @Override
    public CartInfoResponse getCartInfoNonAuthorized(List<CartItemDto> cartItems) {
        return getCartInfoResponse(cartItems);
    }

    @Override
    public void checkAvailability(UUID id, int quantity) {
        getAmountAvailable(id,quantity);
    }

    @Transactional
    @Override
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

    @Transactional
    @Override
    public void removeFromCart(String email, CartItemDto item) {
        UUID productId = item.getProductId();
        int quantityToRemove = item.getQuantity();
        UUID customerId = userService.findByEmail(email).getUserId();
        CartItemEntity cartItem = cartItemDao.getCartItemByProductAndCustomer(customerId, productId)
                .orElseThrow(() -> {
                    throw new IllegalStateException(
                            String.format("There is no product with id %s in your cart", productId)
                    );});
        UUID cartItemId = cartItem.getCartItemId();
        int quantityInCart = cartItem.getQuantity();
        int quantityLeft = quantityInCart - quantityToRemove;
        if(quantityLeft > 0) {
            getAmountAvailable(productId, quantityLeft);
            cartItemDao.changeQuantityById(quantityLeft, cartItemId);
        }
        else if (quantityLeft == 0) cartItemDao.removeFromCart(cartItemId);
        else throw new IllegalStateException(
                String.format("You have %d items of that product in cart.\nYou cannot change the quantity to %d",
                        quantityInCart, quantityLeft));
    }

    private CartInfoResponse getCartInfoResponse(List<CartItemDto> cartItems) {
        List<CartProductInfo> productInfos = cartItems.stream()
                .map(this::CartProductInfoMapper).collect(Collectors.toList());
        int summaryPrice = productInfos.stream().mapToInt(CartProductInfo::getTotalPrice).sum();
        int summaryPriceWithoutDiscount = productInfos.stream().mapToInt(CartProductInfo::getTotalPriceWithoutDiscount).sum();

        return CartInfoResponse.builder()
                .content(productInfos)
                .summaryPrice(summaryPrice)
                .summaryPriceWithoutDiscount(summaryPriceWithoutDiscount)
                .build();
    }

    private CartProductInfo CartProductInfoMapper(CartItemDto cartItem) {
        UUID productId = cartItem.getProductId();
        int quantity = cartItem.getQuantity();
        getAmountAvailable(productId, quantity);
        var product = productService.findProductById(productId).get();
        var categoryName = categoryService.findNameById(product.getCategoryId());
        var productInfo =
                CartProductInfo.builder()
                        .productId(productId)
                        .name(product.getName())
                        .description(product.getDescription())
                        .imageUrl(product.getImageUrl())
                        .price(product.getPrice())
                        .quantity(quantity)
                        .category(categoryName);
        int totalPriceWithoutDiscount = product.getPrice() * quantity;
        productInfo.totalPriceWithoutDiscount(totalPriceWithoutDiscount);
        productService.findActiveProductDiscount(cartItem.getProductId()).ifPresentOrElse(
                discount -> {
                    int offeredPrice = discount.getOfferedPrice();
                    productInfo
                            .discount(offeredPrice)
                            .totalPrice(offeredPrice * quantity);
                },
                () -> productInfo
                        .discount(-1)
                        .totalPrice(totalPriceWithoutDiscount)
        );
        return productInfo.build();
    }

    private int getAmountAvailable(UUID id, int quantity) {
        ProductEntity product = checkForExistenceAndReturn(id);
        checkIfProductIsActive(product);
        return getAmountAvailable(product, quantity);
    }

    private int getAmountAvailable(ProductEntity product, int quantity) {
        int amountAvailable = product.getInStock() - product.getReserved();
        if (amountAvailable < quantity) {
            throw new IllegalStateException(String.format(NOT_SO_MUCH_IN_STOCK, amountAvailable));
        }
        return amountAvailable;
    }

    private ProductEntity checkForExistenceAndReturn(UUID id) {
        return productService
                .findProductById(id)
                .orElseThrow(() -> {
                    throw new IllegalStateException(String.format("Product with id %s not found", id));
                });
    }

    private void checkIfProductIsActive(ProductEntity product) {
        if(!product.getIsActive())
            throw new IllegalStateException(String.format("Product with id %s is not available now",
                    product.getProductId()));
    }

    private boolean checkAmountAvailable(ProductEntity product, int quantity) {
        int amountAvailable = product.getInStock() - product.getReserved();
        return amountAvailable >= quantity;
    }
}
