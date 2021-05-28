package com.netcrackerg4.marketplace.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcrackerg4.marketplace.model.dto.product.NewProductDto;
import com.netcrackerg4.marketplace.service.interfaces.IProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/manager")
@AllArgsConstructor
public class ManagerController {
    private final IProductService productService;

    @PostMapping("/add-product")
    void createProduct(@RequestParam(value = "file") MultipartFile multipartFile,
                       @RequestParam(value = "product")  String product) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        NewProductDto newProductDto = mapper.readValue(product, NewProductDto.class);

        productService.addProduct(multipartFile, newProductDto);
    }

    @PutMapping("/products/{id}/edit-info")
    public void updateProductInfo(@PathVariable UUID id,
                                  @Valid  @RequestBody NewProductDto changeProductDto){

        productService.updateProductInfo(id,changeProductDto);
    }

    @PutMapping("/products/{id}/edit-picture")
    public void updateProductPicture(@PathVariable UUID id,
                                     @RequestParam(value = "file") MultipartFile multipartFile){

        productService.updateProductPicture(id,multipartFile);
    }
}
