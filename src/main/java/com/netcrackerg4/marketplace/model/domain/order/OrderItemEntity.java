package com.netcrackerg4.marketplace.model.domain.order;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrderItemEntity {
    private UUID orderItemId;
    private int quantity;
    private int pricePerProduct;
    private UUID productId;
    private String productName;
    private String productDescription;
}
