package com.netcrackerg4.marketplace.config.postgres_queries;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "custom.postgres.discount")
public class DiscountQueries {
    private String findActiveProductDiscount;
    private String findUnexpiredDiscounts;
    private String checkPeriod;
    private String checkPeriodEdit;
    private String createDiscount;
    private String readDiscount;
    private String updateDiscount;
    private String deleteDiscount;
}
