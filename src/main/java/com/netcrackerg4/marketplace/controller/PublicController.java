package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.domain.CategoryEntity;
import com.netcrackerg4.marketplace.model.domain.DiscountEntity;
import com.netcrackerg4.marketplace.model.domain.ProductEntity;
import com.netcrackerg4.marketplace.model.dto.product.ProductSearchFilter;
import com.netcrackerg4.marketplace.model.response.CategoryResponse;
import com.netcrackerg4.marketplace.model.response.ProductResponse;
import com.netcrackerg4.marketplace.service.interfaces.ICategoryService;
import com.netcrackerg4.marketplace.service.interfaces.IProductService;
import com.netcrackerg4.marketplace.util.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/public")
@RequiredArgsConstructor
public class PublicController {
    private final IProductService productService;
    private final ICategoryService categoryService;

    @GetMapping("/products")
    public List<ProductResponse> getProducts(){
    return  productService.getAll();
   }

    @PostMapping("/products")
    public Page<ProductResponse> getProductPage(@RequestBody @Valid ProductSearchFilter filter, @RequestParam int pageSize, @RequestParam int pageN)
    {
        return  productService.findProducts(filter,pageSize,pageN);
    }
    @GetMapping("/product-page")
    public Page<ProductResponse> getProductPage(@RequestParam int page,@RequestParam int size)
    {
        return  productService.findProducts(page,size);
    }
    @GetMapping("/categories")
    public List<CategoryResponse> getCategories(){
        return  productService.getCategories();
    }

    @GetMapping("/categories-all")
    public List<CategoryEntity> fetchCategories() { return categoryService.getAll(); }

    @GetMapping("/products/{id}")
    public Optional<ProductEntity> findProductById(@PathVariable("id") UUID productId){
      return productService.findProductById(productId);
    }

    @GetMapping("/products/{productId}/active-discount")
    Optional<DiscountEntity> getActiveDiscount(@PathVariable UUID productId){
      return productService.findActiveProductDiscount(productId);
    }

    @GetMapping("/categories/{productId}/category-name")
    String getCategoryNameByProductId(@PathVariable("productId") UUID productId){
      return  categoryService.getCategoryNameByProductId(productId);
    }
}
