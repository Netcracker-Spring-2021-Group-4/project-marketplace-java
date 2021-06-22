package com.netcrackerg4.marketplace.config.postgres_queries;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "custom.postgres.cart")
public class CartQueries {
    private String addToCart;
    private String getByCustomerProduct;
    private String changeQuantityById;
    private String findAuthCustomerCartItems;
    private String reserveProduct;
    private String cancelReservation;
    private String removeFromCart;
    private String resetCart;
    private String findCartItemsByOrderId;

}
