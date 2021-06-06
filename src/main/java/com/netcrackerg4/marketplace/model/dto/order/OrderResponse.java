package com.netcrackerg4.marketplace.model.dto.order;

import com.netcrackerg4.marketplace.model.dto.timestamp.DateTimeSlot;
import com.netcrackerg4.marketplace.model.dto.user.UserCoreView;
import com.netcrackerg4.marketplace.model.enums.OrderStatus;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
public class OrderResponse {
    private UUID orderId;
    private Timestamp placedAt;
    private String phoneNumber;
    @Nullable
    private String comment;
    @Nullable
    private String firstName;
    @Nullable
    private String lastName;
    private OrderStatus status;
    private AddressDto address;
    @Nullable
    private UserCoreView customer;
    private List<OrderItemResponse> orderItems;
    private DateTimeSlot dateTimeSlot;
}
