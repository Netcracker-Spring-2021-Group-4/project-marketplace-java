package com.netcrackerg4.marketplace.model.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CartProductInfo {
    private UUID productId;
    private String name;
    private String description;
    private String imageUrl;
    private int price;
    private String category;
    private int discount;
    private int totalPriceWithoutDiscount;
    private int totalPrice;
}
