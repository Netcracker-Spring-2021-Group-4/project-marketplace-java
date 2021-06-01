package com.netcrackerg4.marketplace.config.postgres_queries;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "custom.postgres.order")
public class OrderQueries {
    private String getTakenTimeslots;
    private String readAllTimeslots;
    private String countActiveCouriers;
}
