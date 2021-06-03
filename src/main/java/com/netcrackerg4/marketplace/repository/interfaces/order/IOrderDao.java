package com.netcrackerg4.marketplace.repository.interfaces.order;

import com.netcrackerg4.marketplace.model.domain.order.OrderEntity;
import com.netcrackerg4.marketplace.repository.interfaces.AbstractCrudRepository;

import java.util.UUID;

public interface IOrderDao extends AbstractCrudRepository<OrderEntity, UUID> {
}
