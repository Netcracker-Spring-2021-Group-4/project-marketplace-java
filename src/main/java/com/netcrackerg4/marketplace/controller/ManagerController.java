package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.dto.product.DiscountDto;
import com.netcrackerg4.marketplace.model.dto.product.NewProductDto;
import com.netcrackerg4.marketplace.service.interfaces.IProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/manager")
@AllArgsConstructor
public class ManagerController {
    private final IProductService productService;

    @PostMapping
            (
                    value = "/add-product",
                    consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
            )
    void createProduct(@RequestPart(value = "file") MultipartFile multipartFile,
                       @RequestPart(value = "product") @Valid NewProductDto product) {
        productService.addProduct(multipartFile, product);
    }

    @PutMapping("/products/{id}/edit-info")
    public void updateProductInfo(@PathVariable UUID id,
                                  @Valid @RequestBody NewProductDto changeProductDto) {
        productService.updateProductInfo(id, changeProductDto);
    }

    @PutMapping("/products/{id}/edit-picture")
    public void updateProductPicture(@PathVariable UUID id,
                                     @RequestParam(value = "file") MultipartFile multipartFile) {
        productService.updateProductPicture(id, multipartFile);
    }

    @PostMapping("/products/discounts")
    void createDiscount(@RequestBody DiscountDto discountDto) {
        productService.addDiscount(discountDto);
    }
}
