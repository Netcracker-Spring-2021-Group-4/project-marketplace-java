package com.netcrackerg4.marketplace.model.dto.order;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderedProductRequest {
    private UUID productId;
    private int quantity;
}
