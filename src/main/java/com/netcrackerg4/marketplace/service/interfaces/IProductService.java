package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.domain.AppProductEntity;
import com.netcrackerg4.marketplace.model.dto.product.NewProductDto;
import com.netcrackerg4.marketplace.model.dto.product.ProductSearchFilter;
import com.netcrackerg4.marketplace.model.dto.user.UserSearchFilter;
import com.netcrackerg4.marketplace.model.response.CategoryResponse;
import com.netcrackerg4.marketplace.model.response.ProductResponse;
import com.netcrackerg4.marketplace.util.EagerContentPage;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProductService {
    void addProduct(MultipartFile multipartFile, NewProductDto newProduct);

    EagerContentPage<ProductResponse> findProducts(ProductSearchFilter searchFilter, int pageSize, int pageN);

    Optional<AppProductEntity> findProductById(UUID id);
    void updateProductInfo(UUID id,  NewProductDto newProduct);
    void updateProductPicture(UUID id, MultipartFile multipartFile);
    List<ProductResponse> getAll();
    List<CategoryResponse> getCategories();

}
