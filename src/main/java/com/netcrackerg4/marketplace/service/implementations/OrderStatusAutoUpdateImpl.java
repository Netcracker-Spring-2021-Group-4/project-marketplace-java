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
import java.util.Comparator;
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
    private final ScheduledExecutorService failedScheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    @Transactional
    public void initSchedulers(Runnable updSubmitted, Runnable updInDelivery) {
        TimeslotEntity[] timeslots = deliverySlotDao.readTimeslotOptions().stream().sorted(Comparator.comparing(TimeslotEntity::getTimeStart)).toArray(TimeslotEntity[]::new);
        if (timeslots.length == 0) {
            System.err.println("Did not find any timeslots, application cannot work without them, exiting...");
            System.exit(-1);
        }
        TimeslotEntity firstSlot = timeslots[0];
        long period = firstSlot.getTimeEnd().toLocalTime()
                .minus(firstSlot.getTimeStart().toLocalTime().toSecondOfDay(), ChronoUnit.SECONDS)
                .toSecondOfDay();

        Optional<LocalTime> nextSlotStart = deliverySlotDao.readTimeslotOptions().stream()
                .filter(x -> {
                    long diff = x.getTimeStart().toLocalTime().toSecondOfDay() - LocalTime.now().toSecondOfDay();
                    return diff > 0 && diff < period;
                })
                .map(TimeslotEntity::getTimeStart)
                .map(Time::toLocalTime)
                .findAny();
        long submittedDelay = nextSlotStart.isPresent()
                ? nextSlotStart.get().toSecondOfDay() - LocalTime.now().toSecondOfDay()
                : LocalTime.MAX.toSecondOfDay() - LocalTime.now().toSecondOfDay() + firstSlot.getTimeStart().toLocalTime().toSecondOfDay();

        Optional<LocalTime> nextSlotEnd = deliverySlotDao.readTimeslotOptions().stream()
                .filter(x -> {
                    long diff = x.getTimeEnd().toLocalTime().toSecondOfDay() - LocalTime.now().toSecondOfDay();
                    return diff > 0 && diff < period;
                })
                .map(TimeslotEntity::getTimeEnd)
                .map(Time::toLocalTime)
                .findAny();
        long inDeliveryDelay = nextSlotEnd.isPresent()
                ? nextSlotEnd.get().toSecondOfDay() - LocalTime.now().toSecondOfDay()
                : LocalTime.MAX.toSecondOfDay() - LocalTime.now().toSecondOfDay() + firstSlot.getTimeEnd().toLocalTime().toSecondOfDay();

        inDeliveryScheduler.scheduleAtFixedRate(updSubmitted, submittedDelay, period, TimeUnit.SECONDS);
        failedScheduler.scheduleAtFixedRate(updInDelivery, inDeliveryDelay, period, TimeUnit.SECONDS);
    }

    @Override
    @Transactional
    public void updateSubmitted() {
        orderDao.updateStatusIfStarted(OrderStatus.SUBMITTED, OrderStatus.IN_DELIVERY);
    }

    @Override
    @Transactional
    public void updateInDelivery() {
        orderDao.updateStatusIfFinished(OrderStatus.IN_DELIVERY, OrderStatus.FAILED);
    }
}
