package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.dto.product.ProductDto;

import java.net.URL;
import java.util.UUID;

public interface IProductService {
    void addProduct(UUID id, URL url, NewProductDto newProduct);
    ProductDto findProductById(UUID id);
}
