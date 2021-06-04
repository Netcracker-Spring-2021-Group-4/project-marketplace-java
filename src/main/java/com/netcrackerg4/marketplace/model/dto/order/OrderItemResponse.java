package com.netcrackerg4.marketplace.model.dto.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderItemResponse {
    private UUID productId;
    private UUID orderItemId;
    private int quantity;
    private int pricePerProduct;
}
