package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.domain.AppProductEntity;
import com.netcrackerg4.marketplace.model.dto.product.NewProductDto;

import java.net.URL;
import java.util.Optional;
import java.util.UUID;

public interface IProductService {
    void addProduct(UUID id, URL url, NewProductDto newProduct);
    Optional<AppProductEntity> findProductById(UUID id);
     void updateProductInfo(UUID id,  NewProductDto newProduct);
     void updateProductPicture(UUID id, URL url);
}
