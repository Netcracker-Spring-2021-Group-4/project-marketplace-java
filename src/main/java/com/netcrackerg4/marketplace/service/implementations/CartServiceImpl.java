package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.exception.productExceptions.ProductNotFoundException;
import com.netcrackerg4.marketplace.model.dto.product.CartItemDto;
import com.netcrackerg4.marketplace.model.dto.product.ProductDto;
import com.netcrackerg4.marketplace.repository.impl.CartItemRepositoryImpl;
import com.netcrackerg4.marketplace.service.interfaces.ICartService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CartServiceImpl implements ICartService {

    private final CartItemRepositoryImpl repository;
    private final ProductServiceImpl productService;

    public CartServiceImpl(CartItemRepositoryImpl repository, ProductServiceImpl productService) {
        this.repository = repository;
        this.productService = productService;
    }

    @Override
    public String addToCart(CartItemDto item) {

        try {
            ProductDto product = productService.findProductById(item.getProductId());
            if(product.getInStock()< item.getQuantity())
                return "There is only "+product.getInStock()+" products in stock! Try buying less ;)";
        }catch(ProductNotFoundException e){
            return "Can not find this product in the database!";
        }

            item.setCartItemId(UUID.randomUUID());
            item.setDateAdded(LocalDateTime.now());
            repository.addToCard(item);

            return "Product was added to cart successfully!";

    }
}
