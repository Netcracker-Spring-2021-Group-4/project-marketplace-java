package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.dto.product.ProductSearchFilter;
import com.netcrackerg4.marketplace.model.response.FilterInfo;
import com.netcrackerg4.marketplace.model.response.ProductResponse;
import com.netcrackerg4.marketplace.service.interfaces.IProductService;
import com.netcrackerg4.marketplace.util.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("api/v1/public")
@RequiredArgsConstructor
public class PublicController {


    private final IProductService productService;


    @PostMapping("/product-page")
    public Page<ProductResponse> getProductPage(@RequestBody @Valid ProductSearchFilter filter, @RequestParam @Min(0) int page, @RequestParam @Min(1) int size)
    {
        return  productService.findProducts(filter,page,size);
    }

    @GetMapping("/product-page")
    public Page<ProductResponse> getProductPage(@RequestParam @Min(0) int page, @RequestParam @Min(1) int size)
    {
        return  productService.findProducts(page,size);
    }
    @GetMapping("/filter-info")
    public FilterInfo getFilterInfo(){
        return  productService.getFilterInfo();
    }

}
