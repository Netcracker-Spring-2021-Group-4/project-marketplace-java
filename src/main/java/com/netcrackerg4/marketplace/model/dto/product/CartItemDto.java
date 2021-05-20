package com.netcrackerg4.marketplace.model.dto.product;

import lombok.Data;
import lombok.NonNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CartItemDto {
    private UUID cartItemId;
    @NonNull
    private int quantity;
    private LocalDateTime dateAdded;
    @NonNull
    private UUID productId;
    private UUID customerId;
}
