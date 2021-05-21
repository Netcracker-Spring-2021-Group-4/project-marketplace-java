package com.netcrackerg4.marketplace.model.dto.product;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Min;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CartItemDto {
    private UUID cartItemId;
    @NonNull
    @Min(value = 1, message = "Quantity must be greater than zero")
    private int quantity;
    private LocalDateTime dateAdded;
    @NonNull
    private UUID productId;
    private UUID customerId;
}
