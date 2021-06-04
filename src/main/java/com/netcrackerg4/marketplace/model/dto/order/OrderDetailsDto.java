package com.netcrackerg4.marketplace.model.dto.order;

import com.netcrackerg4.marketplace.model.domain.AddressEntity;
import com.netcrackerg4.marketplace.model.domain.order.OrderItemEntity;
import com.netcrackerg4.marketplace.model.domain.user.AppUserEntity;
import com.netcrackerg4.marketplace.model.dto.timestamp.DateTimeSlot;
import com.netcrackerg4.marketplace.model.enums.OrderStatus;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
public class OrderDetailsDto {
    private UUID orderId;
    private Timestamp placedAt;
    private String phoneNumber;
    @Nullable
    private String comment;
    private OrderStatus status;
    private AddressEntity address;
    @Nullable
    private AppUserEntity customer;
    private List<OrderItemEntity> orderItems;
    private DateTimeSlot dateTimeSlot;
}
