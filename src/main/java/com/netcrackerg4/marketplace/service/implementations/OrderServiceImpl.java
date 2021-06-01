package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.model.domain.order.OrderEntity;
import com.netcrackerg4.marketplace.model.domain.order.TimeslotEntity;
import com.netcrackerg4.marketplace.model.dto.order.OrderRequest;
import com.netcrackerg4.marketplace.model.dto.timestamp.StatusTimestampDto;
import com.netcrackerg4.marketplace.model.enums.OrderStatus;
import com.netcrackerg4.marketplace.repository.interfaces.IDeliverySlotDao;
import com.netcrackerg4.marketplace.service.interfaces.IOrderService;
import com.netcrackerg4.marketplace.util.EagerContentPage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements IOrderService {
    private final IDeliverySlotDao deliverySlotDao;

    @Override
    public List<StatusTimestampDto> getDayTimeslots(LocalDate date) {
        List<TimeslotEntity> timeslots = deliverySlotDao.readTimeslotOptions();
        int activeCouriers = deliverySlotDao.countActiveCouriers();
        Map<LocalTime, Integer> takenSlots = deliverySlotDao.readTakenTimeslots(date);

        return timeslots.stream().map(ts ->
                new StatusTimestampDto(ts.getTimeStart().toLocalTime(), ts.getTimeEnd().toLocalTime(),
                        activeCouriers - takenSlots.getOrDefault(ts.getTimeStart().toLocalTime(), 0) == 0))
                .collect(Collectors.toList());
    }

    @Override
    public void makeOrder(OrderRequest orderRequest, UUID customerId) {

    }

    @Override
    public void setOrderStatus(UUID orderId, OrderStatus newStatus) {

    }

    @Override
    public EagerContentPage<OrderRequest> getActiveOrders(UUID courierId) {
        return null;
    }

    @Override
    public EagerContentPage<OrderEntity> getOrdersPage(int page, UUID customerId) {
        return null;
    }
}
