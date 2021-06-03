package com.netcrackerg4.marketplace.service.interfaces;

<<<<<<< HEAD
=======
import com.netcrackerg4.marketplace.model.domain.DiscountEntity;
>>>>>>> develop
import com.netcrackerg4.marketplace.model.domain.ProductEntity;
import com.netcrackerg4.marketplace.model.dto.product.DiscountDto;
import com.netcrackerg4.marketplace.model.dto.product.NewProductDto;
import com.netcrackerg4.marketplace.model.dto.product.ProductSearchFilter;
import com.netcrackerg4.marketplace.model.response.FilterInfo;
import com.netcrackerg4.marketplace.model.response.ProductResponse;
import com.netcrackerg4.marketplace.util.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProductService {
    void addProduct(MultipartFile multipartFile, NewProductDto newProduct);

    Page<ProductResponse> findProducts(ProductSearchFilter searchFilter, int pageSize, int pageN);

    Optional<ProductEntity> findProductById(UUID id);
    void updateProductInfo(UUID id,  NewProductDto newProduct);
    void updateProductPicture(UUID id, MultipartFile multipartFile);
    List<ProductResponse> getAll();
<<<<<<< HEAD
    Page<ProductResponse> findProducts(int page, int size);
    FilterInfo getFilterInfo();

    void editDiscount(UUID productId, UUID discountId, DiscountDto discountDto);
=======
    List<CategoryResponse> getCategories();
    Page<ProductResponse> findProducts(int page, int size);
    Optional<DiscountEntity> findActiveProductDiscount(UUID productId);
    List<DiscountEntity> getUnexpiredDiscounts(UUID productId);
    void addDiscount(UUID productId, DiscountDto discountDto);
    void editDiscount(UUID productId, UUID discountId, DiscountDto discountDto);
    void removeDiscount(UUID discountId);
>>>>>>> develop
}
