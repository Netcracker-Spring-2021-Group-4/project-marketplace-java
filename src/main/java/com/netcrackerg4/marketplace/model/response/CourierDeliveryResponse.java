package com.netcrackerg4.marketplace.model.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.netcrackerg4.marketplace.config.postgres_queries.order.OrderQueries;
import com.netcrackerg4.marketplace.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CourierDeliveryResponse {
    private UUID orderId;
    private Time timeStart;
    private Time timeEnd;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String city;
    private String street;
    private String building;
    private int flat;
    private OrderStatus statusName;






}
