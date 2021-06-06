package com.netcrackerg4.marketplace.model.domain.order;

import com.netcrackerg4.marketplace.model.domain.AddressEntity;
import com.netcrackerg4.marketplace.model.domain.user.AppUserEntity;
import com.netcrackerg4.marketplace.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    private UUID orderId;
    private Timestamp placedAt;
    private String phoneNumber;
    @Nullable
    private String firstName;
    @Nullable
    private String lastName;
    @Nullable
    private String comment;
    private OrderStatus status;
    private AddressEntity address;
    @Nullable
    private AppUserEntity customer;
    private List<OrderItemEntity> orderItems;
}
