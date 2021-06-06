package com.netcrackerg4.marketplace.model.dto.order;

import com.netcrackerg4.marketplace.model.domain.order.OrderItemEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDetails {
    private List<OrderItemEntity> orderItems;
    private LocalDate datestamp;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private AddressDto address;
    private String phoneNumber;
    @Nullable
    private String comment;
    private String customerFirstName;
    private String customerLastName;
}
