package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.response.CourierOrderResponse;

import java.time.LocalDate;
import java.util.List;

public interface ICourierService {

    List<CourierOrderResponse> getDayTimeslots(LocalDate date, String email);
}
