package com.netcrackerg4.marketplace.config.postgres_queries.order;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "custom.postgres.order-item")
public class OrderItemQueries {
    private String createOrderItem;
    private String readOrderItem;
    private String readItemsOfOrder;
}
