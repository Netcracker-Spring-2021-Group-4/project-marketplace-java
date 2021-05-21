package com.netcrackerg4.marketplace.model.dto.product;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data
public class ProductDto {
  private UUID productId;
  private String productName;
  private String description;
  @Min(value = 0, message = "Price can not be negative value")
  private double price;
  @Min(value = 0, message = "In stock can not be negative value")
  private int inStock;
  @Min(value = 0, message = "Reserved can not be negative value")
  private int reserved;
  private LocalDate availabilityDate;
  private boolean isActive;
  private int categoryId;
  
}
