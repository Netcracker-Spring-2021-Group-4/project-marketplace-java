package com.netcrackerg4.marketplace.config.postgres_queries;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "custom.postgres.product")
public class ProductQueries {
    private String findCategoryIdByName;
    private String createProduct;
}
