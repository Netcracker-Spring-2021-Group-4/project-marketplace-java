package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.domain.AppProductEntity;
import com.netcrackerg4.marketplace.model.dto.product.NewProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.Optional;
import java.util.UUID;

public interface IProductService {
    void addProduct(MultipartFile multipartFile, NewProductDto newProduct);
    Optional<AppProductEntity> findProductById(UUID id);
    void updateProductInfo(UUID id,  NewProductDto newProduct);
    void updateProductPicture(UUID id, MultipartFile multipartFile);
}
