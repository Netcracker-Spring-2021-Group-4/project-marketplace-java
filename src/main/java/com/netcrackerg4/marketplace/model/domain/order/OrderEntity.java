package com.netcrackerg4.marketplace.model.domain.order;

import com.netcrackerg4.marketplace.model.domain.user.AppUserEntity;
import com.netcrackerg4.marketplace.model.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OrderEntity {
    private UUID orderId;
    private Timestamp placedAt;
    private String phoneNumber;
    @Nullable
    private String comment;
    private OrderStatus status;
    private String address;
    @Nullable
    private AppUserEntity user;
    private List<OrderItemEntity> orderItems;
}
