package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.model.domain.AppProductEntity;
import com.netcrackerg4.marketplace.model.dto.product.NewProductDto;
import com.netcrackerg4.marketplace.repository.interfaces.IProductDao;
import com.netcrackerg4.marketplace.service.interfaces.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements IProductService {
    private final IProductDao productDao;

    @Transactional
    @Override
    public void addProduct(String url, NewProductDto newProduct) {

        AppProductEntity productEntity = AppProductEntity.builder()
                .name(newProduct.getProductName())
                .description(newProduct.getDescription())
                .imageUrl(url)
                .price(newProduct.getPrice())
                .inStock(newProduct.getInStock())
                .reserved(newProduct.getReserved())
                .availabilityDate(newProduct.getDate())
                .isActive(Boolean.TRUE)
                .category(newProduct.getProductCategory())
                .build();
        productDao.createProduct(productEntity);

    }
}
