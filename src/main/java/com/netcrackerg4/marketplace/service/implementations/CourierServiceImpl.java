package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.model.domain.order.DeliverySlotEntity;
import com.netcrackerg4.marketplace.service.interfaces.ICourierService;
import com.netcrackerg4.marketplace.service.interfaces.IMailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CourierServiceImpl implements ICourierService {
    private final IMailService mailService;

    @Override
    public void assignCourier(DeliverySlotEntity deliverySlot) {
        // todo: implement
        mailService.notifyCourierGotDelivery(null, null);
    }
}
