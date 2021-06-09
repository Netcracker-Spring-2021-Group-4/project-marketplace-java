package com.netcrackerg4.marketplace.repository.interfaces.order;

import com.netcrackerg4.marketplace.model.domain.order.DeliverySlotEntity;
import com.netcrackerg4.marketplace.model.domain.order.TimeslotEntity;
import com.netcrackerg4.marketplace.model.domain.user.AppUserEntity;
import com.netcrackerg4.marketplace.model.dto.timestamp.DateTimeSlot;
import com.netcrackerg4.marketplace.model.response.CourierDeliveryResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public interface IDeliverySlotDao {
    void create(DeliverySlotEntity deliverySlot);

    Collection<TimeslotEntity> readTimeslotOptions();

    Map<LocalTime, Integer> readTakenTimeslots(LocalDate date);

    int countActiveCouriers();

    Optional<AppUserEntity> findFreeCourier(LocalDateTime deliveryTimeSlot);

    Optional<DateTimeSlot> findSlotByOrder(UUID orderId);

    List<CourierDeliveryResponse> getCourierSlots(UUID id, LocalDate date);

}
