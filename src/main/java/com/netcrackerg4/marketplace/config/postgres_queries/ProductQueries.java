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
    private String activeProductsWithDiscount;

    private String productsWithFiltersOrderByPriceAsc;
    private String productsWithFiltersOrderByPriceDesc;
    private String productsWithFiltersOrderByName;
    private String productsWithFiltersOrderByDate;

    private String productsPage;
    private String maxPrice;

}
