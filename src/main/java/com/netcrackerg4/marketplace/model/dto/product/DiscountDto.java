package com.netcrackerg4.marketplace.model.dto.product;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DiscountDto {
    private int offeredPrice;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private UUID targetProduct;
}
