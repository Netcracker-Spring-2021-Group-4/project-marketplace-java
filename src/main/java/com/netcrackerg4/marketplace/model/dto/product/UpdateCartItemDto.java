package com.netcrackerg4.marketplace.model.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Min;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCartItemDto {
    @Min(value = 0, message = "Quantity must be not negative")
    private int quantity;
    @NonNull
    private UUID productId;
}
