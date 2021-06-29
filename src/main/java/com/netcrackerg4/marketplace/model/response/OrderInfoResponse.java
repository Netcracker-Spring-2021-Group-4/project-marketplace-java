package com.netcrackerg4.marketplace.model.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.netcrackerg4.marketplace.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OrderInfoResponse {
    private UUID orderId;
    private Timestamp placedAt;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String city;
    private String street;
    private String building;
    private Integer flat;
    private OrderStatus statusName;
    private String comment;
    private List<ОrderProductInfo> content;
    private int summaryPrice;
}
