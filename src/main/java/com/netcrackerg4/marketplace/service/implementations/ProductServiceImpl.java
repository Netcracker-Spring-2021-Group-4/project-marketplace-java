package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.model.domain.AppProductEntity;
import com.netcrackerg4.marketplace.model.dto.product.NewProductDto;
import com.netcrackerg4.marketplace.repository.interfaces.IProductDao;
import com.netcrackerg4.marketplace.service.interfaces.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final IProductDao productDao;

    @Transactional
    @Override
    public void addProduct(UUID id, URL url, NewProductDto newProduct) {

        AppProductEntity productEntity = AppProductEntity.builder()
                .productId(id)
                .name(newProduct.getProductName())
                .description(newProduct.getDescription())
                .imageUrl(url.toString())
                .price(newProduct.getPrice())
                .inStock(newProduct.getInStock())
                .reserved(newProduct.getReserved())
                .availabilityDate(new Date())
                .isActive(Boolean.TRUE)
                .categoryId(newProduct.getCategoryId())
                .build();
        productDao.create(productEntity);

    }

    @Transactional
    @Override
    public void updateProductInfo(UUID id,  NewProductDto newProduct) {

        if (findProductById(id).isEmpty())
            throw new IllegalStateException("There is no product with such id");

        AppProductEntity productEntity = AppProductEntity.builder()
                .productId(id)
                .name(newProduct.getProductName())
                .description(newProduct.getDescription())
                .price(newProduct.getPrice())
                .inStock(newProduct.getInStock())
                .reserved(newProduct.getReserved())
                .availabilityDate(new Date())
                .categoryId(newProduct.getCategoryId())
                .build();
        productDao.update(productEntity);
    }

    @Transactional
    @Override
    public void updateProductPicture(UUID id, URL url) {
        productDao.updatePicture(id,url);
    }

    @Override
    public Optional<AppProductEntity> findProductById(UUID id) {
        return productDao.read(id);
    }
}
