package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.domain.AppProductEntity;
import com.netcrackerg4.marketplace.model.dto.product.NewProductDto;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProductService {
    void addProduct(UUID id, URL url, NewProductDto newProduct);
    Optional<AppProductEntity> findProductById(UUID id);
    void deactivateProduct(UUID productId);
    List<AppProductEntity> getAllProducts();
}
