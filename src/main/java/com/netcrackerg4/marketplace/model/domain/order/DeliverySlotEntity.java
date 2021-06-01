package com.netcrackerg4.marketplace.model.domain.order;

import com.netcrackerg4.marketplace.model.domain.user.AppUserEntity;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.util.UUID;

@Data
@Builder
public class DeliverySlotEntity {
    private UUID deliverySlotId;
    private Date datestamp;
    private TimeslotEntity timeslotEntity;
    private AppUserEntity courier;
    // todo: move ownership of datestamp to order
    private OrderEntity order;
}
