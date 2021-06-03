package com.netcrackerg4.marketplace.repository.interfaces.order;

import com.netcrackerg4.marketplace.model.domain.order.TimeslotEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface IDeliverySlotDao {
    List<TimeslotEntity> readTimeslotOptions();

    Map<LocalTime, Integer> readTakenTimeslots(LocalDate date);

    int countActiveCouriers();
}
