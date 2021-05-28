package com.netcrackerg4.marketplace.controller;


import com.netcrackerg4.marketplace.model.domain.AppProductEntity;
import com.netcrackerg4.marketplace.service.interfaces.IProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/public")
@AllArgsConstructor
public class ProductController {
  private final IProductService productService;


  @GetMapping("/product/all")
  public List<AppProductEntity> getAllProducts(){
    return productService.getAllProducts();
  }

  @GetMapping("/product/{id}")
  public Optional<AppProductEntity> getProductById(@PathVariable("id") UUID productId){
    return productService.findProductById(productId);
  }
}
