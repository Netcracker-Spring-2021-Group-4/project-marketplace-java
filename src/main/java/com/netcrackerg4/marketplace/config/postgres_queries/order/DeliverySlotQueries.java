package com.netcrackerg4.marketplace.config.postgres_queries.order;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "custom.postgres.delivery-slot")
public class DeliverySlotQueries {
    private String readTakenTimeslots;
    private String readAllTimeslots;
    private String countActiveCouriers;
    private String createDeliverySlot;
    private String findFreeCourier;
    private String findDeliverySlotByOrderId;
    private String checkSlotIsAvailable;
}
