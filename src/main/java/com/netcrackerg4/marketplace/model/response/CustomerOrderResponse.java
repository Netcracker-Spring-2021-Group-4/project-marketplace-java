package com.netcrackerg4.marketplace.model.response;

import com.netcrackerg4.marketplace.model.dto.order.AddressDto;
import com.netcrackerg4.marketplace.model.dto.timestamp.DateTimeSlot;
import com.netcrackerg4.marketplace.model.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CustomerOrderResponse {
    private UUID orderId;
    private DateTimeSlot deliverySlot;
    private AddressDto deliveryAddress;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private OrderStatus status;
    private String comment;
    private LocalDateTime placedAt;
}
