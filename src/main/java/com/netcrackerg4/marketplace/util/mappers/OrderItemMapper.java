package com.netcrackerg4.marketplace.util.mappers;

import com.netcrackerg4.marketplace.model.domain.order.OrderItemEntity;
import com.netcrackerg4.marketplace.model.dto.order.OrderItemResponse;
import org.springframework.beans.BeanUtils;

public class OrderItemMapper {
    public static OrderItemResponse entityToResponse(OrderItemEntity orderItemEntity) {
        OrderItemResponse orderItem = new OrderItemResponse();
        BeanUtils.copyProperties(orderItemEntity, orderItem);
        return orderItem;
    }
}
