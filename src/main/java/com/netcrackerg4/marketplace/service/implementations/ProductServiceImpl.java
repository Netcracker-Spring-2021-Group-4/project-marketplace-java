package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.model.domain.DiscountEntity;
import com.netcrackerg4.marketplace.model.domain.ProductEntity;
import com.netcrackerg4.marketplace.model.dto.product.DiscountDto;
import com.netcrackerg4.marketplace.model.dto.product.NewProductDto;
import com.netcrackerg4.marketplace.repository.interfaces.IDiscountDao;
import com.netcrackerg4.marketplace.repository.interfaces.IProductDao;
import com.netcrackerg4.marketplace.service.interfaces.IProductService;
import com.netcrackerg4.marketplace.service.interfaces.IS3Service;
import com.netcrackerg4.marketplace.util.mappers.DiscountEntity_Dao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final IProductDao productDao;
    private final IS3Service s3Service;
    private final IDiscountDao discountDao;
    private final DiscountEntity_Dao discountMapper;

    @Transactional
    @Override
    public void addProduct(MultipartFile multipartFile, NewProductDto newProduct) {

        UUID id = UUID.randomUUID();
        URL url = s3Service.uploadImage(id, multipartFile);

        ProductEntity productEntity = ProductEntity.builder()
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

    @Override
    public Optional<ProductEntity> findProductById(UUID id) {
        return productDao.read(id);
    }

    @Transactional
    @Override
    public void updateProductPicture(UUID id, MultipartFile multipartFile) {
        findProductById(id)
                .orElseThrow(() -> new IllegalStateException("There is no product with such id."));

        URL url = s3Service.uploadImage(id, multipartFile);
        productDao.updatePicture(id, url);
    }

    @Transactional
    @Override
    public void updateProductInfo(UUID id, NewProductDto newProduct) {

        findProductById(id)
                .orElseThrow(() -> new IllegalStateException("There is no product with such id."));
        ProductEntity productEntity = ProductEntity.builder()
                .productId(id)
                .name(newProduct.getProductName())
                .description(newProduct.getDescription())
                .price(newProduct.getPrice())
                .inStock(newProduct.getInStock())
                .reserved(newProduct.getReserved())
                .categoryId(newProduct.getCategoryId())
                .build();
        productDao.update(productEntity);
    }

    public Optional<DiscountEntity> findActiveProductDiscount(UUID id) {
        return discountDao.findActiveProductDiscount(id);
    }

    @Override
    public List<DiscountEntity> getUnexpiredDiscounts(UUID productId) {
        return discountDao.findUnexpiredDiscounts(productId);
    }

    @Override
    @Transactional
    public void addDiscount(DiscountDto discountRequest) {
        DiscountEntity discountEntity = discountMapper.toDiscountEntity(discountRequest);
        discountEntity.setDiscountId(UUID.randomUUID());
        discountDao.create(discountEntity);
    }

    @Override
    public void editDiscount(DiscountEntity discount) {
        discountDao.update(discount);
    }

    @Override
    public void removeDiscount(UUID discountId) {
        discountDao.delete(discountId);
    }
}
