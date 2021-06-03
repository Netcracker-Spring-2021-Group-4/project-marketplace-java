package com.netcrackerg4.marketplace.repository.interfaces;


import com.netcrackerg4.marketplace.model.domain.ProductEntity;
import com.netcrackerg4.marketplace.model.response.CategoryResponse;
import com.netcrackerg4.marketplace.model.response.ProductResponse;

import java.net.URL;
import java.util.List;
import java.util.UUID;

public interface IProductDao extends AbstractCrudRepository<ProductEntity, UUID>{
    void updatePicture(UUID key, URL url);

    List<ProductResponse> findAll();
    List<ProductResponse> findAll(int p, int s);

    List<ProductResponse> findProductsWithFilters(String query, List<Integer> categories,Double from,Double to, String sortBy, int pageSize,int pageN );

    List<CategoryResponse> findCategories();

    void activateDeactivateProduct(ProductEntity product);


}
