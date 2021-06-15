package com.netcrackerg4.marketplace.util.mappers;

import com.netcrackerg4.marketplace.model.domain.order.OrderEntity;
import com.netcrackerg4.marketplace.model.response.CustomerOrderResponse;
import org.springframework.beans.BeanUtils;

import java.time.ZoneOffset;

public class OrderEntity_CustomerResponse {
    public static CustomerOrderResponse toOrderResponse(OrderEntity orderEntity) {
        CustomerOrderResponse orderResponse = new CustomerOrderResponse();
        BeanUtils.copyProperties(orderEntity, orderResponse);
        orderResponse.setDeliveryAddress(AddressMapper.entityToDto(orderEntity.getAddress()));
        orderResponse.setPlacedAt(orderEntity.getPlacedAt().toLocalDateTime().toEpochSecond(ZoneOffset.ofHours(3)) * 1000);
        return orderResponse;
    }
}
