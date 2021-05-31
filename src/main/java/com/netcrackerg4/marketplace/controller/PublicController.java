package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.dto.product.ProductSearchFilter;
import com.netcrackerg4.marketplace.model.response.CategoryResponse;
import com.netcrackerg4.marketplace.model.response.ProductResponse;
import com.netcrackerg4.marketplace.service.interfaces.IProductService;
import com.netcrackerg4.marketplace.util.EagerContentPage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/public")
@RequiredArgsConstructor
public class PublicController {


    private final IProductService productService;

    @GetMapping("/products")
    public List<ProductResponse> getProducts(){
    return  productService.getAll();
   }

    @PostMapping("/products")
    public EagerContentPage<ProductResponse> getProductPage(@RequestBody @Valid ProductSearchFilter filter, @RequestParam int pageSize, @RequestParam int pageN)
    {
        return  productService.findProducts(filter,pageSize,pageN);
    }
    @GetMapping("/categories")
    public List<CategoryResponse> getCategories(){
        return  productService.getCategories();
    }

}
