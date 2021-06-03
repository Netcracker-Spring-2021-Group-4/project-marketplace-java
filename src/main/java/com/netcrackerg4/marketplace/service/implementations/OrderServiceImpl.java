package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.exception.BadCodeError;
import com.netcrackerg4.marketplace.model.domain.AddressEntity;
import com.netcrackerg4.marketplace.model.domain.order.OrderEntity;
import com.netcrackerg4.marketplace.model.domain.order.OrderItemEntity;
import com.netcrackerg4.marketplace.model.domain.order.TimeslotEntity;
import com.netcrackerg4.marketplace.model.domain.product.DiscountEntity;
import com.netcrackerg4.marketplace.model.domain.product.ProductEntity;
import com.netcrackerg4.marketplace.model.dto.order.OrderRequest;
import com.netcrackerg4.marketplace.model.dto.order.OrderedProductRequest;
import com.netcrackerg4.marketplace.model.dto.timestamp.StatusTimestampDto;
import com.netcrackerg4.marketplace.model.enums.OrderStatus;
import com.netcrackerg4.marketplace.repository.interfaces.IDiscountDao;
import com.netcrackerg4.marketplace.repository.interfaces.IProductDao;
import com.netcrackerg4.marketplace.repository.interfaces.IUserDao;
import com.netcrackerg4.marketplace.repository.interfaces.order.IAddressDao;
import com.netcrackerg4.marketplace.repository.interfaces.order.IDeliverySlotDao;
import com.netcrackerg4.marketplace.repository.interfaces.order.IOrderDao;
import com.netcrackerg4.marketplace.service.interfaces.IOrderService;
import com.netcrackerg4.marketplace.util.EagerContentPage;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements IOrderService {
    private final IDeliverySlotDao deliverySlotDao;
    private final IUserDao userDao;
    private final IOrderDao orderDao;
    private final IProductDao productDao;
    private final IDiscountDao discountDao;
    private final IAddressDao addressDao;

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
    @Transactional
    public void makeOrder(OrderRequest orderRequest, UUID maybeCustomerId) {
        Map<UUID, ProductEntity> loadedProducts = handleStocks(orderRequest.getProducts());

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(UUID.randomUUID());
        orderEntity.setPlacedAt(Timestamp.from(Instant.now()));
        orderEntity.setPhoneNumber(orderRequest.getPhoneNumber());
        orderEntity.setComment(orderRequest.getComment());
        orderEntity.setStatus(OrderStatus.SUBMITTED);

        AddressEntity address = new AddressEntity();
        BeanUtils.copyProperties(orderRequest.getAddress(), address);
        address.setAddressId(UUID.randomUUID());
        if (maybeCustomerId != null) {
            address.setCustomerId(maybeCustomerId);
            orderEntity.setCustomer(userDao.read(maybeCustomerId)
                    .orElseThrow(() -> new BadCodeError("customer making the order should have been present")));
        }

        addressDao.create(address);
        orderEntity.setAddress(address);

        orderEntity.setOrderItems(orderRequest.getProducts().stream().map(reqItem ->
                OrderItemEntity.builder()
                        .orderItemId(UUID.randomUUID())
                        .productId(reqItem.getProductId())
                        .quantity(reqItem.getQuantity())
                        .pricePerProduct(getProductPrice(reqItem.getProductId(), loadedProducts))
                        .orderId(orderEntity.getOrderId())
                        .build())
                .collect(Collectors.toList()));

        orderDao.create(orderEntity);
    }

    private Map<UUID, ProductEntity> handleStocks(List<OrderedProductRequest> orderItems) {
        Map<UUID, ProductEntity> productEntityMap = new HashMap<>(orderItems.size());
        for (OrderedProductRequest item : orderItems) {
            ProductEntity product = productDao.read(item.getProductId()).orElseThrow();
            productEntityMap.put(product.getProductId(), product);
            if (product.getReserved() < item.getQuantity())
                throw new IllegalStateException("There should have been more products reserved.");
            product.setReserved(product.getReserved() - item.getQuantity());
            product.setInStock(product.getInStock() - item.getQuantity());
            productDao.update(product);
        }
        return productEntityMap;
    }

    private int getProductPrice(UUID productId, Map<UUID, ProductEntity> productEntityMap) {
        Optional<DiscountEntity> maybeDiscount = discountDao.findActiveProductDiscount(productId);
        return maybeDiscount.map(DiscountEntity::getOfferedPrice)
                .orElseGet(() -> productEntityMap.get(productId).getPrice());
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
