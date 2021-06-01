package com.netcrackerg4.marketplace.config.postgres_queries;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "custom.postgres.category")
public class CategoryQueries {
    private String findById;
}
