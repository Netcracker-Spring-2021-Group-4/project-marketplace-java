package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.domain.user.AppUserEntity;
import com.netcrackerg4.marketplace.model.dto.order.OrderRequest;
import com.netcrackerg4.marketplace.model.dto.order.OrderResponse;
import com.netcrackerg4.marketplace.model.dto.timestamp.StatusTimestampDto;
import com.netcrackerg4.marketplace.model.enums.OrderStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IOrderService {
    List<StatusTimestampDto> getDayTimeslots(LocalDate date);

    void makeOrder(OrderRequest orderRequest, AppUserEntity maybeCustomer);

    void setOrderStatus(UUID orderId, OrderStatus newStatus);

    List<OrderResponse> getCourierOrders(UUID courierId);

}
