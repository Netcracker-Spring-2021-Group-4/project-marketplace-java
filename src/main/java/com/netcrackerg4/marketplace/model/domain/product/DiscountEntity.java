package com.netcrackerg4.marketplace.model.domain.product;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DiscountEntity {
    private UUID discountId;
    private int offeredPrice;
    private Timestamp startsAt;
    private Timestamp endsAt;
    private UUID productId;
}
