package com.netcrackerg4.marketplace.model.dto.order;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class OrderItemRequest {
    @NotNull(message = "product must be specified")
    private UUID productId;
    @NotNull
    @Min(value = 1, message = "quantity of a product must be > 0")
    private int quantity;
}
