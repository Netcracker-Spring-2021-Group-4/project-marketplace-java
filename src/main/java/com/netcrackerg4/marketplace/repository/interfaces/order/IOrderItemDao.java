package com.netcrackerg4.marketplace.repository.interfaces.order;

import com.netcrackerg4.marketplace.model.domain.order.OrderItemEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IOrderItemDao {
    void create(OrderItemEntity orderItem);

    void createItemsOfOrder(Collection<OrderItemEntity> orderItems);

    Optional<OrderItemEntity> read(UUID itemId);

    List<OrderItemEntity> readItemsOfOrder(UUID orderId);
}
