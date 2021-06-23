package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.domain.product.DiscountEntity;
import com.netcrackerg4.marketplace.model.dto.product.DiscountDto;
import com.netcrackerg4.marketplace.model.dto.product.NewProductDto;
import com.netcrackerg4.marketplace.service.interfaces.IProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
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

    @PatchMapping("/products/{productId}/activate-deactivate")
    void activateDeactivateProduct(@PathVariable("productId") UUID productId) {
        productService.activateDeactivateProduct(productId);
    }

    @PostMapping("/products/{productId}/discounts")
    void createDiscount(@PathVariable UUID productId, @RequestBody @Valid DiscountDto discountDto) {
        if (discountDto.getStartsAt().after(discountDto.getEndsAt()))
            throw new IllegalStateException("start time is after end time");
        productService.addDiscount(productId, discountDto);
    }

    @PutMapping("/products/{productId}/discounts/{discountId}")
    void editDiscount(@PathVariable UUID productId, @PathVariable UUID discountId,
                      @RequestBody @Valid DiscountDto discountDto) {
        productService.editDiscount(productId, discountId, discountDto);
    }

    @GetMapping("/products/{productId}/unexpired-discounts")
    List<DiscountEntity> getFutureDiscounts(@PathVariable UUID productId) {
        return productService.getUnexpiredDiscounts(productId);
    }

    @DeleteMapping("/products/discounts/{discountId}")
    void deleteDiscount(@PathVariable UUID discountId) {
        productService.removeDiscount(discountId);
    }
}

