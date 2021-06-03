package com.netcrackerg4.marketplace.config.postgres_queries.order;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "custom.postgres.delivery-slot")
public class DeliverySlotQueries {
    private String getTakenTimeslots;
    private String readAllTimeslots;
    private String countActiveCouriers;
}
