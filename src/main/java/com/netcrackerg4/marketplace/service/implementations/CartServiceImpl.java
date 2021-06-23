package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.model.domain.product.CartItemEntity;
import com.netcrackerg4.marketplace.model.domain.product.ProductEntity;
import com.netcrackerg4.marketplace.model.dto.ContentErrorListWrapper;
import com.netcrackerg4.marketplace.model.dto.ContentErrorWrapper;
import com.netcrackerg4.marketplace.model.dto.product.CartItemDto;
import com.netcrackerg4.marketplace.model.response.CartInfoResponse;
import com.netcrackerg4.marketplace.model.response.CartProductInfo;
import com.netcrackerg4.marketplace.repository.interfaces.ICartItemDao;
import com.netcrackerg4.marketplace.service.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
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

    @Override
    public ContentErrorWrapper<Integer> checkProductAvailability(UUID productId) {
        return getAmountAvailableWithoutThrow(productId);
    }

    @Transactional
    @Override
    public ContentErrorWrapper<Boolean> addToCart(String email, CartItemDto item) {
        UUID productId = item.getProductId();
        var amountAvailableWrapper = getAmountAvailableWithoutThrow(item.getProductId());
        if(amountAvailableWrapper.getError() != null)
            return new ContentErrorWrapper<>(false, amountAvailableWrapper.getError());

        var productName = productService.findProductById(productId).orElseThrow().getName();
        int amountAvailable = amountAvailableWrapper.getContent();
        if (amountAvailable <= 0)
            return new ContentErrorWrapper<>(false, getErrorMessageNoAvailability(productName));

        UUID customerId = userService.findByEmail(email).orElseThrow().getUserId();
        var addingToCartQuantity = Math.min(item.getQuantity(), amountAvailable);
        var existingCartItem = cartItemDao.getCartItemByProductAndCustomer(customerId, productId);
        var resultValue = new ContentErrorWrapper<>(true, null);
        existingCartItem
                .ifPresentOrElse(
                        cartItemEntity -> {
                            UUID id = cartItemEntity.getCartItemId();
                            int overallAdding = cartItemEntity.getQuantity() + addingToCartQuantity;
                            if (overallAdding > amountAvailable)
                                resultValue.setError(String
                                        .format("Your cart now stores the maximum of %d items of product with name \"%s\"",
                                        amountAvailable, productName));
                            else if (item.getQuantity() > amountAvailable)
                                resultValue.setError(getErrorMessageAddingMaxAvailable
                                        (item.getQuantity(), productName, amountAvailable));

                            int newQuantity = Math.min(overallAdding, amountAvailable);
                            cartItemDao.changeQuantityById(newQuantity, id);
                        },
                        () -> {
                            if(item.getQuantity() > amountAvailable) {
                                resultValue.setError(getErrorMessageAddingMaxAvailable(
                                        item.getQuantity(), productName, amountAvailable));
                            }
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
        return resultValue;
    }

    @Transactional
    @Override
    public List<String> addToCartList(String email, List<CartItemDto> items) {
        return items.stream()
                .map(i -> addToCart(email, i).getError())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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
            var amountAvailableWrapper = getAmountAvailableWithoutThrow(productId);
            if(amountAvailableWrapper.getError() != null) return false;
            int quantity = Math.min(quantityLeft, amountAvailableWrapper.getContent());
            cartItemDao.changeQuantityById(quantity, cartItemId);
        } else cartItemDao.removeFromCart(cartItemId);
        return true;
    }

    @Override
    public ContentErrorListWrapper<CartInfoResponse> getCartInfoNonAuthorized(List<CartItemDto> cartItems) {
        return getCartInfoResponse(cartItems);
    }

    @Override
    public ContentErrorListWrapper<CartInfoResponse> getCartInfoAuthorized(String email) {
        UUID userId = userService.findByEmail(email).orElseThrow().getUserId();
        List<CartItemDto> cartItems = cartItemDao.getAuthCustomerCartItems(userId);
        return getCartInfoResponse(cartItems);
    }

    private ContentErrorListWrapper<CartInfoResponse> getCartInfoResponse(List<CartItemDto> cartItems) {
        List<ContentErrorWrapper<CartProductInfo>> mapperResults = cartItems.stream()
                .map(this::cartProductInfoMapper).collect(Collectors.toList());
        var productInfos = mapperResults.stream()
                .map(ContentErrorWrapper::getContent)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        int summaryPrice = productInfos.stream().mapToInt(CartProductInfo::getTotalPrice).sum();
        int summaryPriceWithoutDiscount = productInfos.stream().mapToInt(CartProductInfo::getTotalPriceWithoutDiscount).sum();

        var content = CartInfoResponse.builder()
                .content(productInfos)
                .summaryPrice(summaryPrice)
                .summaryPriceWithoutDiscount(summaryPriceWithoutDiscount)
                .build();
        var errors = mapperResults.stream()
                .map(ContentErrorWrapper::getError)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return new ContentErrorListWrapper<>(content, errors);
    }

    private ContentErrorWrapper<CartProductInfo> cartProductInfoMapper(CartItemDto cartItem) {
        var result = new ContentErrorWrapper<CartProductInfo>();
        UUID productId = cartItem.getProductId();
        int quantity = cartItem.getQuantity();
        var amountAvailableWrapper = getAmountAvailableWithoutThrow(productId);
        if (amountAvailableWrapper.getError() != null)
            return new ContentErrorWrapper<>(null, amountAvailableWrapper.getError());
        var product = productService.findProductById(productId).orElseThrow();
        var productName = product.getName();
        var amountAvailable = amountAvailableWrapper.getContent();
        if (amountAvailable <= 0)
            return new ContentErrorWrapper<>(null, getErrorMessageNoAvailability(productName));

        if (quantity > amountAvailable)
            result.setError(getErrorMessageAddingMaxAvailable(quantity, productName, amountAvailable));

        int finalQuantity = Math.min(quantity, amountAvailable);
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
        var content = productInfo.build();
        result.setContent(content);
        return result;
    }

    private String getErrorMessageNoAvailability(String productName) {
        return String.format("Items of product with name \"%s\"  are not available", productName);
    }

    private String getErrorMessageAddingMaxAvailable(int quantity, String productName, int available) {
        return String.format("Cannot add %d items of product with name \"%s\" to the cart, adding %d items",
                quantity, productName, available);
    }

    private ContentErrorWrapper<Integer> getAmountAvailableWithoutThrow(UUID id) {
        ContentErrorWrapper<Integer> returnValue = new ContentErrorWrapper<>();
        productService
            .findProductById(id)
            .ifPresentOrElse(product -> {
                if(!product.getIsActive()) {
                    returnValue.setError(String.format("Product with name \"%s\" is not active now", product.getName()));
                    return;
                }
                returnValue.setContent(product.getInStock() - product.getReserved());
            }, () -> returnValue.setError(String.format("Product with id %s not found", id))
            );
        return returnValue;
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
