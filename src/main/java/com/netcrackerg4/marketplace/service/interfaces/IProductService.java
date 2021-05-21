package com.netcrackerg4.marketplace.service.interfaces;


import com.netcrackerg4.marketplace.model.dto.product.ProductDto;

import java.util.UUID;

public interface IProductService {
    void addProduct(ProductDto p);
    ProductDto findProductById(UUID id);
}
