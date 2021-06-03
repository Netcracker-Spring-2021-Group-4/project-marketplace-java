package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.domain.order.DeliverySlotEntity;

public interface ICourierService {
    void assignCourier(DeliverySlotEntity deliverySlot);
}
