package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.domain.DiscountEntity;
import com.netcrackerg4.marketplace.model.domain.ProductEntity;
import com.netcrackerg4.marketplace.model.dto.product.DiscountDto;
import com.netcrackerg4.marketplace.model.dto.product.NewProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProductService {
    void addProduct(MultipartFile multipartFile, NewProductDto newProduct);

    Optional<ProductEntity> findProductById(UUID id);

    void updateProductInfo(UUID id, NewProductDto newProduct);

    void updateProductPicture(UUID id, MultipartFile multipartFile);

    Optional<DiscountEntity> findActiveProductDiscount(UUID productId);

    List<DiscountEntity> getUnexpiredDiscounts(UUID productId);

    void addDiscount(DiscountDto discountRequest);

    void editDiscount(DiscountEntity discount);

    void removeDiscount(UUID discountId);
}
