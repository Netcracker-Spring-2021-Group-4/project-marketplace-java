package com.netcrackerg4.marketplace.model.dto.order;

import com.netcrackerg4.marketplace.model.domain.order.OrderItemEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class DeliveryDetails {
    private UUID orderId;
    private List<OrderItemEntity> orderItems;
    private LocalDate datestamp;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private String address;
    private String phoneNumber;
    @Nullable
    private String comment;
    private String customerFirstName;
    private String customerLastName;
}
