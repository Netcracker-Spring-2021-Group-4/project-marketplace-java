package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.model.domain.order.TimeslotEntity;
import com.netcrackerg4.marketplace.model.enums.OrderStatus;
import com.netcrackerg4.marketplace.repository.interfaces.order.IDeliverySlotDao;
import com.netcrackerg4.marketplace.repository.interfaces.order.IOrderDao;
import com.netcrackerg4.marketplace.service.interfaces.IOrderStatusAutoUpdate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class OrderStatusAutoUpdateImpl implements IOrderStatusAutoUpdate {
    private final IDeliverySlotDao deliverySlotDao;
    private final IOrderDao orderDao;

    private final ScheduledExecutorService inDeliveryScheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    @Transactional
    public void initSchedulers(Runnable updSubmitted) {
        Optional<TimeslotEntity> maybeFstSlot = deliverySlotDao.readTimeslotOptions().stream()
                .min(TimeslotEntity.getStartComparator());
        if (maybeFstSlot.isEmpty()) {
            System.err.println("Did not find any maybeFstSlot, application cannot work without them, exiting...");
            System.exit(-1);
        }
        TimeslotEntity firstSlot = maybeFstSlot.get();
        long period = firstSlot.getTimeEnd().toLocalTime()
                .minus(firstSlot.getTimeStart().toLocalTime().toSecondOfDay(), ChronoUnit.SECONDS)
                .toSecondOfDay();

        Optional<TimeslotEntity> nextSlot = deliverySlotDao.readTimeslotOptions().stream()
                .filter(timeslot -> {
                    long diff = timeslot.getTimeStart().toLocalTime().toSecondOfDay() - LocalTime.now().toSecondOfDay();
                    return diff > 0 && diff < period;
                })
                .findAny();

        Optional<LocalTime> nextSlotStart = nextSlot.map(TimeslotEntity::getTimeStart).map(Time::toLocalTime);

        long submittedDelay = nextSlotStart.isPresent()
                ? nextSlotStart.get().toSecondOfDay() - LocalTime.now().toSecondOfDay()
                : LocalTime.MAX.toSecondOfDay() - LocalTime.now().toSecondOfDay() + firstSlot.getTimeStart().toLocalTime().toSecondOfDay();

        inDeliveryScheduler.scheduleAtFixedRate(updSubmitted, submittedDelay, period, TimeUnit.SECONDS);
    }

    @Override
    @Transactional
    public void updateSubmitted() {
        orderDao.updateStatusIfStarted(OrderStatus.SUBMITTED, OrderStatus.IN_DELIVERY);
    }
}
