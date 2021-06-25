package com.netcrackerg4.marketplace.repository.interfaces;


import com.netcrackerg4.marketplace.model.domain.product.ProductEntity;

import com.netcrackerg4.marketplace.model.domain.product.RecommendationEntity;
import com.netcrackerg4.marketplace.model.enums.SortingOptions;
import com.netcrackerg4.marketplace.model.response.ProductResponse;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface IProductDao extends AbstractCrudRepository<ProductEntity, UUID>{
     void updatePicture(UUID key, URL url);
     List<ProductResponse> findAll(int p, int s);
     Integer maxPrice();
     List<ProductResponse> findProductsWithFilters(String query, List<Integer> categories, int from, int to, SortingOptions sortBy, int pageSize, int pageN );
     int findAllSize();
     int findAllFilteredSize(String query, List<Integer> categories, int from, int to);
     void activateDeactivateProduct(ProductEntity product);
     Optional<ProductResponse> findProductForComparison(UUID id);
     List<ProductResponse> usuallyBuyThisProductWith(UUID productId, int amount);

     Map<UUID,Integer> getAllProductsSupport();
     int getProductsSupport(UUID productX, UUID productY);
     void updateRecommendations(List<RecommendationEntity> recommends);
     void clearPopularNow();
     void updatePopularNow(List<UUID> ids);
     List<UUID> popularNowIds(int limit);


}
