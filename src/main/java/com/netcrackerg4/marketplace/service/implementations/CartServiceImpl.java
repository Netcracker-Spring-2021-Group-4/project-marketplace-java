package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.model.domain.product.CartItemEntity;
import com.netcrackerg4.marketplace.model.domain.product.ProductEntity;
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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

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
            () -> cartItems
                    .stream()
                    .sorted(Comparator.comparing(CartItemDto::getProductId))
                    .forEach(i -> cartItemDao.reserveProduct(i.getQuantity(), i.getProductId()))
        );

    }

    @Override
    @Transactional
    public void cancelCartReservation(List<CartItemDto> cartItems) {
        cartItems
                .stream()
                .sorted(Comparator.comparing(CartItemDto::getProductId))
                .forEach( i -> {
                    var product = checkForExistenceAndReturn(i.getProductId());
                    if(!product.getIsActive()) return;
                    int setMinus = i.getQuantity();
                    if(product.getReserved() - setMinus < 0) setMinus = product.getReserved();
                    cartItemDao.cancelReservation(setMinus, product.getProductId());
                });
    }

    @Transactional
    @Override
    public boolean addToCart(String email, CartItemDto item) {
        UUID productId = item.getProductId();
        int amountAvailable = getAmountAvailableWithoutThrow(item.getProductId());
        if (amountAvailable <= 0) return false;
        UUID customerId = userService.findByEmail(email).orElseThrow().getUserId();
        var existingCartItem = cartItemDao.getCartItemByProductAndCustomer(customerId, productId);
        AtomicBoolean success = new AtomicBoolean(true);
        existingCartItem
                .ifPresentOrElse(
                        cartItemEntity -> {
                            UUID id = cartItemEntity.getCartItemId();
                            int addingQuantity = cartItemEntity.getQuantity() + item.getQuantity();
                            int newQuantity = Math.min(addingQuantity, amountAvailable);
                            if (addingQuantity > amountAvailable) success.set(false);
                            cartItemDao.changeQuantityById(newQuantity, id);
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
        return success.get();
    }

    @Override
    public CartInfoResponse getCartInfoNonAuthorized(List<CartItemDto> cartItems) {
        return getCartInfoResponse(cartItems);
    }

    @Transactional
    @Override
    public void addToCartListIfPossible(String email, List<CartItemDto> items) {
        items.forEach(item -> {
            UUID productId = item.getProductId();
            var available = getAmountAvailableWithoutThrow(productId);
            if (available <= 0) return;
            var addingToCartQuantity = Math.min(item.getQuantity(), available);
            UUID customerId = userService.findByEmail(email).orElseThrow().getUserId();
            cartItemDao
                    .getCartItemByProductAndCustomer(customerId, productId)
                    .ifPresentOrElse(
                            cartItemEntity -> {
                                UUID id = cartItemEntity.getCartItemId();
                                int addingQuantity = Math.min(available,
                                        addingToCartQuantity + cartItemEntity.getQuantity());
                                cartItemDao.changeQuantityById(addingQuantity, id);
                            },
                            () -> {
                                CartItemEntity cartItemEntity =
                                        CartItemEntity.builder()
                                                .cartItemId(UUID.randomUUID())
                                                .customerId(customerId)
                                                .productId(productId)
                                                .quantity(addingToCartQuantity)
                                                .timestampAdded(new Timestamp(System.currentTimeMillis()))
                                                .build();

                                cartItemDao.addToCart(cartItemEntity);
                            }
                    );
        });
    }

    @Transactional
    @Override
    public boolean removeFromCart(String email, CartItemDto item) {
        UUID productId = item.getProductId();
        int quantityToRemove = item.getQuantity();
        UUID customerId = userService.findByEmail(email).orElseThrow().getUserId();
        var cartItemEntityOptional =
                cartItemDao.getCartItemByProductAndCustomer(customerId, productId);
        if(cartItemEntityOptional.isEmpty()) return false;
        var cartItem = cartItemEntityOptional.get();
        UUID cartItemId = cartItem.getCartItemId();
        int quantityInCart = cartItem.getQuantity();
        int quantityLeft = quantityInCart - quantityToRemove;
        if (quantityLeft > 0) {
            int amountAvailable = getAmountAvailableWithoutThrow(productId);
            int quantity = Math.min(quantityLeft, amountAvailable);
            cartItemDao.changeQuantityById(quantity, cartItemId);
        } else cartItemDao.removeFromCart(cartItemId);
        return true;
    }

    @Override
    public CartInfoResponse getCartInfoAuthorized(String email) {
        UUID userId = userService.findByEmail(email).orElseThrow().getUserId();
        List<CartItemDto> cartItems = cartItemDao.getAuthCustomerCartItems(userId);
        return getCartInfoResponse(cartItems);
    }

    private CartInfoResponse getCartInfoResponse(List<CartItemDto> cartItems) {
        List<CartProductInfo> productInfos = cartItems.stream()
                .map(this::cartProductInfoMapper).filter(Objects::nonNull).collect(Collectors.toList());
        int summaryPrice = productInfos.stream().mapToInt(CartProductInfo::getTotalPrice).sum();
        int summaryPriceWithoutDiscount = productInfos.stream().mapToInt(CartProductInfo::getTotalPriceWithoutDiscount).sum();

        return CartInfoResponse.builder()
                .content(productInfos)
                .summaryPrice(summaryPrice)
                .summaryPriceWithoutDiscount(summaryPriceWithoutDiscount)
                .build();
    }

    private CartProductInfo cartProductInfoMapper(CartItemDto cartItem) {
        UUID productId = cartItem.getProductId();
        int quantity = cartItem.getQuantity();
        int amountAvailable = getAmountAvailableWithoutThrow(productId);
        if (amountAvailable <= 0) return null;
        int finalQuantity = Math.min(quantity, amountAvailable);
        var product = productService.findProductById(productId).get();
        var categoryName = categoryService.findNameById(product.getCategoryId());
        var productInfo =
                CartProductInfo.builder()
                        .productId(productId)
                        .name(product.getName())
                        .description(product.getDescription())
                        .imageUrl(product.getImageUrl())
                        .price(product.getPrice())
                        .quantity(finalQuantity)
                        .category(categoryName);
        int totalPriceWithoutDiscount = product.getPrice() * finalQuantity;
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

    private int getAmountAvailableWithoutThrow(UUID id) {
        AtomicInteger result = new AtomicInteger();
        productService
            .findProductById(id)
            .ifPresent(product -> {
                if(product.getIsActive()) result.set(product.getInStock() - product.getReserved());
            });
        return result.get();
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
}
