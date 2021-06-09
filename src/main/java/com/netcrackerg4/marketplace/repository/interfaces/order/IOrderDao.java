package com.netcrackerg4.marketplace.repository.interfaces.order;

import com.netcrackerg4.marketplace.model.domain.order.OrderEntity;
import com.netcrackerg4.marketplace.model.enums.OrderStatus;
import com.netcrackerg4.marketplace.repository.interfaces.AbstractCrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IOrderDao extends AbstractCrudRepository<OrderEntity, UUID> {
    List<OrderEntity> readCourierOrders(UUID courierId, List<OrderStatus> orderStatuses, int pageSize, int pageNo);

    int countCourierOrders(UUID courierId, List<OrderStatus> orderStatuses);

    List<OrderEntity> readCustomerOrders(UUID customerId, List<OrderStatus> orderStatuses, int pageSize, int pageNo);

    int countCustomerOrders(UUID customerId, List<OrderStatus> orderStatuses);

    Collection<UUID> updateStatusIfStarted(OrderStatus current, OrderStatus next);

    Collection<UUID> updateStatusIfFinished(OrderStatus current, OrderStatus next);
}
