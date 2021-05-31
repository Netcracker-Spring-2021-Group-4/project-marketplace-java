package com.netcrackerg4.marketplace.model.dto.product;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Min;
import java.util.UUID;

@Data
@NoArgsConstructor
@Builder
public class CartItemDto {
    @Min(value = 1, message = "Quantity must be greater than zero")
    private int quantity;
    @NonNull
    private UUID productId;
}
