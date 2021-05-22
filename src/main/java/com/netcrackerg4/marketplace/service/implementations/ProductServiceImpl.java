package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.exception.productExceptions.ProductNotFoundException;
import com.netcrackerg4.marketplace.model.dto.product.ProductDto;
import com.netcrackerg4.marketplace.repository.interfaces.IProductDao;
import com.netcrackerg4.marketplace.service.interfaces.IProductService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl implements IProductService {

    private final IProductDao repository;

    public ProductServiceImpl(IProductDao repository) {
        this.repository = repository;
    }


    @Override
    public void addProduct(ProductDto p) {
        p.setProductId(UUID.randomUUID());
        repository.create(p);
    }

    @Override
    public ProductDto findProductById(UUID id) {
        return repository.read(id).orElseThrow(ProductNotFoundException::new);
    }
}
