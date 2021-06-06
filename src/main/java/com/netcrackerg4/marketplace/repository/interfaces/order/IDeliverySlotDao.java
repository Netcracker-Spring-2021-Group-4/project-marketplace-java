package com.netcrackerg4.marketplace.repository.interfaces.order;

import com.netcrackerg4.marketplace.model.domain.order.DeliverySlotEntity;
import com.netcrackerg4.marketplace.model.domain.order.TimeslotEntity;
import com.netcrackerg4.marketplace.model.domain.user.AppUserEntity;
import com.netcrackerg4.marketplace.model.dto.timestamp.DateTimeSlot;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface IDeliverySlotDao {
    void create(DeliverySlotEntity deliverySlot);

    Collection<TimeslotEntity> readTimeslotOptions();

    Map<LocalTime, Integer> readTakenTimeslots(LocalDate date);

    int countActiveCouriers();

    Optional<AppUserEntity> findFreeCourier(LocalDateTime deliveryTimeSlot);

    Optional<DateTimeSlot> findSlotByOrder(UUID orderId);

    boolean deliverySlotIsFree(Date date, Time startTime);
}
