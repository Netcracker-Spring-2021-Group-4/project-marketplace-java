package com.netcrackerg4.marketplace.repository.interfaces;

import com.netcrackerg4.marketplace.model.response.CourierOrderResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ICourierDao {
    List<CourierOrderResponse> getCourierSlots(UUID id, LocalDate date);
}
