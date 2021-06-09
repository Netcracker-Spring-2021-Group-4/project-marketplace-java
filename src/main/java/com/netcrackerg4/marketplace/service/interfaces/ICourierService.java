package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.response.CourierDeliveryResponse;

import java.time.LocalDate;
import java.util.List;

public interface ICourierService {

    List<CourierDeliveryResponse> getDayTimeslots(LocalDate date, String email);
}
