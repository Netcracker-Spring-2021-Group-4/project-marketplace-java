package com.netcrackerg4.marketplace.config.postgres_queries.order;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "custom.postgres.order")
public class OrderQueries {
    private String createOrder;

    private String readOrder;
    private String readStatusIds;

    private String findCourierOrders;
    private String findCustomerOrders;
    private String findOrderDetails;

    private String checkCourierAssigned;
    private String checkCustomerMade;

    private String countCourierOrdersNum;
    private String countCustomerOrdersNum;

    private String updateStatus;
    private String updateStatusWithinSlot;
    private String updateStatusAfterSlot;
}
