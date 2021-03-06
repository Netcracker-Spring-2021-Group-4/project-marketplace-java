package com.netcrackerg4.marketplace.config.postgres_queries;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "custom.postgres.product")
public class ProductQueries {
    private String findProductById;
    private String findInStockById;
    private String createProduct;
    private String updateProductInfo;
    private String updateProductPicture;
    private String activeProductsSize;
    private String activeProductsFilteredSize;
    private String productsWithFiltersOrderByPriceAsc;
    private String productsWithFiltersOrderByPriceDesc;
    private String productsWithFiltersOrderByName;
    private String productsWithFiltersOrderByDate;
    private String productsWithFiltersOrderByPopularity;
    private String productForComparisonById;
    private String maxPrice;
    private String activateDeactivateProduct;


    //Move to SuggestionsProductQueries ??
    private String popularNow;
    private String clearPopularNow;
    private String updatePopularNow;

    private String productsSupport;
    private String productFrequency;
    private String allProductSupport;

    private String  productRecommendations;
    private String  insertRecommendation;
    private String  deleteRecommendations;

}
