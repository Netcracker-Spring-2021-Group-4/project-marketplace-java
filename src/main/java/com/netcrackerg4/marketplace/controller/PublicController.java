package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.response.ProductResponse;
import com.netcrackerg4.marketplace.service.interfaces.IProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/public")
public class PublicController {


    private final IProductService productService;

    @GetMapping("/products")
    public List<ProductResponse> getProductPage(){
    return  productService.getAll();
   }

}
