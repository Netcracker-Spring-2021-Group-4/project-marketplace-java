package com.netcrackerg4.marketplace.controller;



import com.netcrackerg4.marketplace.model.domain.DiscountEntity;
import com.netcrackerg4.marketplace.model.domain.ProductEntity;
import com.netcrackerg4.marketplace.service.interfaces.IProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/public/product")
@AllArgsConstructor
public class  ProductController {
  private final IProductService productService;


  @GetMapping("/{id}")
  public Optional<ProductEntity> findProductById(@PathVariable("id") UUID productId){
    return productService.findProductById(productId);
  }

  @GetMapping("/{productId}/active-discount")
  Optional<DiscountEntity> getActiveDiscount(@PathVariable UUID productId){
    return productService.findActiveProductDiscount(productId);
  }

}
