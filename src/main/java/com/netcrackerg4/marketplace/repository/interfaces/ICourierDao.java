package com.netcrackerg4.marketplace.repository.interfaces;

import com.netcrackerg4.marketplace.model.response.CourierDeliveryResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ICourierDao {
    List<CourierDeliveryResponse> getCourierSlots(UUID id, LocalDate date);
}
