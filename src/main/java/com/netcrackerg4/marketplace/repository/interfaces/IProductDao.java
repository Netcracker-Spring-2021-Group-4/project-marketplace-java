package com.netcrackerg4.marketplace.repository.interfaces;


import com.netcrackerg4.marketplace.model.domain.product.ProductEntity;

import com.netcrackerg4.marketplace.model.enums.SortingOptions;
import com.netcrackerg4.marketplace.model.response.ProductResponse;

import java.net.URL;
import java.util.List;
import java.util.UUID;

public interface IProductDao extends AbstractCrudRepository<ProductEntity, UUID>{
     void updatePicture(UUID key, URL url);

     List<ProductResponse> findAll(int p, int s);
     Integer maxPrice();
     List<ProductResponse> findProductsWithFilters(String query, List<Integer> categories, int from, int to, SortingOptions sortBy, int pageSize, int pageN );


     int findAllSize();
     int findAllFilteredSize(String query, List<Integer> categories, int from, int to);

}
