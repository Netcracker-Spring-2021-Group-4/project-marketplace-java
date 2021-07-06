package com.netcrackerg4.marketplace.service.implementations;


import com.netcrackerg4.marketplace.model.domain.product.DiscountEntity;
import com.netcrackerg4.marketplace.model.domain.product.ProductEntity;
import com.netcrackerg4.marketplace.model.dto.product.DiscountDto;
import com.netcrackerg4.marketplace.model.dto.product.NewProductDto;
import com.netcrackerg4.marketplace.model.dto.product.ProductSearchFilter;
import com.netcrackerg4.marketplace.model.enums.SortingOptions;
import com.netcrackerg4.marketplace.model.response.FilterInfo;
import com.netcrackerg4.marketplace.model.response.ProductResponse;
import com.netcrackerg4.marketplace.repository.interfaces.IAdvLockUtil;
import com.netcrackerg4.marketplace.repository.interfaces.IDiscountDao;
import com.netcrackerg4.marketplace.repository.interfaces.IProductDao;
import com.netcrackerg4.marketplace.service.interfaces.ICategoryService;
import com.netcrackerg4.marketplace.service.interfaces.IProductService;
import com.netcrackerg4.marketplace.service.interfaces.IS3Service;
import com.netcrackerg4.marketplace.util.Page;
import com.netcrackerg4.marketplace.util.mappers.DiscountEntity_Dao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final IProductDao productDao;
    private final IS3Service s3Service;
    private final ICategoryService categoryService;
    private final IDiscountDao discountDao;
    private final DiscountEntity_Dao discountMapper;
    private final IAdvLockUtil advLockUtil;

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
        advLockUtil.requestTransactionLock(id.getMostSignificantBits());
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
    public FilterInfo getFilterInfo() {

        List<FilterInfo.CategoryResponse> categories = categoryService.categoriesWithAmountOfProduct();

        int maxPrice = productDao.findAllSize()!=0 ? productDao.maxPrice():0;

        return new FilterInfo(categories,maxPrice);
    }

    @Override
    public List<ProductResponse> getListOfProductForComparison(List<UUID> ids) {
        return ids.stream()
            .map( id -> {
                var productResponseOptional = productDao.findProductForComparison(id);
                if(productResponseOptional.isEmpty()) return null;
                var productResponse = productResponseOptional.get();
                discountDao.findActiveProductDiscount(id)
                    .ifPresentOrElse(
                        discount -> productResponse.setDiscount(discount.getOfferedPrice()),
                        () -> productResponse.setDiscount(-1));
                return productResponse;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
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
        validateDiscount(productId, discountDto.getOfferedPrice());
        DiscountEntity discountEntity = discountMapper.toDiscountEntity(discountDto);
        discountEntity.setProductId(productId);
        discountEntity.setDiscountId(UUID.randomUUID());
        discountDao.create(discountEntity);
    }

    private void validateDiscount(UUID productId, int offeredPrice) {
        int currPrice = productDao.read(productId).orElseThrow().getPrice();
        if (currPrice <= offeredPrice) throw new IllegalStateException("Discount must be... a discount");
    }

    @Override
    @Transactional
    public void editDiscount(UUID productId, UUID discountId, DiscountDto discountDto) {
        validateDiscount(productId, discountDto.getOfferedPrice());
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
    public void activateDeactivateProduct(UUID productId) {
        ProductEntity product = findProductById(productId)
            .orElseThrow(() -> new IllegalStateException("There is no product with such id."));
        if(product.getIsActive()) {
            product.setReserved(0);
        }
        else{
            product.setAvailabilityDate(new Date());
        }
        productDao.activateDeactivateProduct(product);
    }




    @Override
    public List<ProductResponse> getSuggestionsForProductBuyWith(UUID productId) {
         findProductById(productId)
                .orElseThrow(() -> new IllegalStateException("There is no product with such id."));

        return productDao.usuallyBuyThisProductWith(productId,6);
    }

    @Transactional
    @Override
    public Page<ProductResponse> findProducts(ProductSearchFilter searchFilter, int pageSize, int pageN) {
        if(searchFilter.getMinPrice()>searchFilter.getMaxPrice())
            throw new IllegalStateException("MinPrice should be <= MaxPrice");

        List<Integer> categoryIds = (searchFilter.getCategoryIds()== null || searchFilter.getCategoryIds().isEmpty()) ? categoryService.getCategoriesIds(): searchFilter.getCategoryIds();
        int from = searchFilter.getMinPrice();
        int to = searchFilter.getMaxPrice()==0 ? productDao.maxPrice() : searchFilter.getMaxPrice();
        String query = searchFilter.getNameQuery() != null ? searchFilter.getNameQuery() : "";
        SortingOptions sortOption = searchFilter.getSortOption() !=null? searchFilter.getSortOption():SortingOptions.POPULARITY;
        List<ProductResponse> content = productDao.findProductsWithFilters(query,categoryIds,from,to,sortOption,pageSize,pageN);
        int size = productDao.findAllFilteredSize(query, categoryIds,from,to);
        return new Page<>(content,size);

    }


        @Override
    public Optional<ProductEntity> findProductById(UUID id) {
        return productDao.read(id);
    }



}
