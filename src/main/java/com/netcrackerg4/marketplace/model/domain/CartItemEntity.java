package com.netcrackerg4.marketplace.model.domain;

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
public class CartItemEntity {
    private UUID cartItemId;
    private int quantity;
    private Timestamp timestampAdded;
    private UUID customerId;
    private UUID productId;
}
