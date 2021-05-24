package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.dto.product.NewProductDto;

public interface IProductService {
    void addProduct(String url, NewProductDto newProduct);
}
