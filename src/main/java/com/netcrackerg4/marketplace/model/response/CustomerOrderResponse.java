package com.netcrackerg4.marketplace.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netcrackerg4.marketplace.model.dto.order.AddressDto;
import com.netcrackerg4.marketplace.model.enums.OrderStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class CustomerOrderResponse {
    private UUID orderId;

    private long deliveryDate;
    private long timeStart;
    private long timeEnd;

    private AddressDto deliveryAddress;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private OrderStatus status;
    private String comment;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, timezone = "UTC")
    private long placedAt;
}
