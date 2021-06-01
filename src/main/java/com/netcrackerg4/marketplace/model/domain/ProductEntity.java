package com.netcrackerg4.marketplace.model.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProductEntity {
    private final UUID productId;
    private String name;
    private String description;
    private String imageUrl;
    private int price;
    private int inStock;
    private int reserved;
    private Date availabilityDate;
    private Boolean isActive;
    private int categoryId;

}