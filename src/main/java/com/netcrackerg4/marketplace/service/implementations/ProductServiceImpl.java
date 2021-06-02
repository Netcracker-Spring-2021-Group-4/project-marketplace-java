package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.model.domain.AppProductEntity;
import com.netcrackerg4.marketplace.model.dto.product.NewProductDto;
import com.netcrackerg4.marketplace.model.dto.product.ProductSearchFilter;
import com.netcrackerg4.marketplace.model.response.CategoryResponse;
import com.netcrackerg4.marketplace.model.response.ProductResponse;
import com.netcrackerg4.marketplace.repository.interfaces.IProductDao;
import com.netcrackerg4.marketplace.service.interfaces.IProductService;
import com.netcrackerg4.marketplace.service.interfaces.IS3Service;
import com.netcrackerg4.marketplace.util.Page;
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

    @Transactional
    @Override
    public void addProduct(MultipartFile multipartFile, NewProductDto newProduct) {

        UUID id = UUID.randomUUID();
        URL url = s3Service.uploadImage(id, multipartFile);

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

        findProductById(id)
                .orElseThrow(() -> new IllegalStateException("There is no product with such id."));
        AppProductEntity productEntity = AppProductEntity.builder()
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

    @Transactional
    @Override
    public void updateProductPicture(UUID id, MultipartFile multipartFile) {
        findProductById(id)
                .orElseThrow(() -> new IllegalStateException("There is no product with such id."));

        URL url = s3Service.uploadImage(id, multipartFile);
        productDao.updatePicture(id,url);
    }

    @Override
    public List<ProductResponse> getAll() {
       return productDao.findAll();
    }

    @Override
    public List<CategoryResponse> getCategories() {
        return productDao.findCategories();
    }

    @Override
    public Page<ProductResponse> findProducts(int page, int size) {
        return new Page<>(productDao.findAll(page, size), getAll().size());
    }

    @Override
    public Page<ProductResponse> findProducts(ProductSearchFilter searchFilter, int pageSize, int pageN) {
        List<Integer> categoryIds = searchFilter.getCategoryIds() ;
        Double from = searchFilter.getMinPrice() != null ? searchFilter.getMinPrice() : 0;
        Double to = searchFilter.getMaxPrice() ;
        String query = searchFilter.getNameQuery() != null ? searchFilter.getNameQuery() : "";
        String sortOption = searchFilter.getSortOption() !=null? searchFilter.getSortOption():"product_name";
        List<ProductResponse> content = productDao.findProductsWithFilters(query,categoryIds,from,to,sortOption,pageSize,pageN);
        return new Page<>(content, content.size());

    }


        @Override
    public Optional<AppProductEntity> findProductById(UUID id) {
        return productDao.read(id);
    }


}
