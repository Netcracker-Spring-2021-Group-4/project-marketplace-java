package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.domain.order.OrderEntity;
import com.netcrackerg4.marketplace.model.dto.order.OrderRequest;
import com.netcrackerg4.marketplace.model.dto.timestamp.SimpleTimestampDto;
import com.netcrackerg4.marketplace.model.enums.OrderStatus;
import com.netcrackerg4.marketplace.util.EagerContentPage;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IOrderService {
    List<SimpleTimestampDto> getDayTimeslots(LocalDate date);

    void makeOrder(OrderRequest orderRequest, @Nullable UUID customerId);

    void setOrderStatus(UUID orderId, OrderStatus newStatus);

    // todo: maybe combine, add filters
    EagerContentPage<OrderRequest> getActiveOrders(UUID courierId);

    EagerContentPage<OrderEntity> getOrdersPage(int page, @Nullable UUID customerId);
}
