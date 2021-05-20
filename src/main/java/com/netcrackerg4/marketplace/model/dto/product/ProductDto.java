package com.netcrackerg4.marketplace.model.dto.product;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class ProductDto {
  private UUID productId;
  private String productName;
  private String description;
  private double price;
  private int inStock;
  private Date availabilityDate;
  private boolean isActive;
  private int categoryId;
  
}
