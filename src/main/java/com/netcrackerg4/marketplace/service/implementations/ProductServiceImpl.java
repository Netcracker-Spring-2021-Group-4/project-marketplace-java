package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.model.domain.DiscountEntity;
import com.netcrackerg4.marketplace.model.domain.ProductEntity;
import com.netcrackerg4.marketplace.model.dto.product.DiscountDto;
import com.netcrackerg4.marketplace.model.dto.product.NewProductDto;
import com.netcrackerg4.marketplace.model.dto.product.ProductSearchFilter;
import com.netcrackerg4.marketplace.model.response.CategoryResponse;
import com.netcrackerg4.marketplace.model.response.ProductResponse;
import com.netcrackerg4.marketplace.repository.interfaces.IDiscountDao;
import com.netcrackerg4.marketplace.repository.interfaces.IProductDao;
import com.netcrackerg4.marketplace.service.interfaces.IProductService;
import com.netcrackerg4.marketplace.service.interfaces.IS3Service;
import com.netcrackerg4.marketplace.util.Page;
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

    @Transactional
    @Override
    public void updateProductInfo(UUID id,  NewProductDto newProduct) {

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
    public Optional<DiscountEntity> findActiveProductDiscount(UUID productId) {
        return discountDao.findActiveProductDiscount(productId);
    }

    @Override
    public List<DiscountEntity> getUnexpiredDiscounts(UUID productId) {
        return discountDao.findUnexpiredDiscounts(productId);
    }

    @Override
    @Transactional
    public void addDiscount(UUID productId, DiscountDto discountDto) {
        DiscountEntity discountEntity = discountMapper.toDiscountEntity(discountDto);
        discountEntity.setProductId(productId);
        discountEntity.setDiscountId(UUID.randomUUID());
        discountDao.create(discountEntity);
    }

    @Override
    @Transactional
    public void editDiscount(UUID productId, UUID discountId, DiscountDto discountDto) {
        DiscountEntity discountEntity = discountMapper.toDiscountEntity(discountDto);
        discountEntity.setProductId(productId);
        discountEntity.setDiscountId(discountId);
        discountDao.update(discountEntity);
    }

    @Override
    @Transactional
    public void removeDiscount(UUID discountId) {
        discountDao.delete(discountId);
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
    public Optional<ProductEntity> findProductById(UUID id) {
        return productDao.read(id);
    }

    @Override
    public void activateDeactivateProduct(UUID productId) {
        ProductEntity product = findProductById(productId)
            .orElseThrow(() -> new IllegalStateException("There is no product with such id."));
        if(product.getIsActive()) {
            product.setReserved(0);
            productDao.activateDeactivateProduct(product);
        }
        else{
            product.setAvailabilityDate(new Date());
            productDao.activateDeactivateProduct(product);
        }
    }


}
