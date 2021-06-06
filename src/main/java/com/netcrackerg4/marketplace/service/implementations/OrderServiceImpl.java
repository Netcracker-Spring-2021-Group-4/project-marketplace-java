package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.model.domain.AddressEntity;
import com.netcrackerg4.marketplace.model.domain.order.DeliverySlotEntity;
import com.netcrackerg4.marketplace.model.domain.order.OrderEntity;
import com.netcrackerg4.marketplace.model.domain.order.OrderItemEntity;
import com.netcrackerg4.marketplace.model.domain.order.TimeslotEntity;
import com.netcrackerg4.marketplace.model.domain.product.DiscountEntity;
import com.netcrackerg4.marketplace.model.domain.product.ProductEntity;
import com.netcrackerg4.marketplace.model.domain.user.AppUserEntity;
import com.netcrackerg4.marketplace.model.dto.order.DeliveryDetails;
import com.netcrackerg4.marketplace.model.dto.order.OrderItemRequest;
import com.netcrackerg4.marketplace.model.dto.order.OrderRequest;
import com.netcrackerg4.marketplace.model.dto.order.OrderResponse;
import com.netcrackerg4.marketplace.model.dto.timestamp.StatusTimestampDto;
import com.netcrackerg4.marketplace.model.enums.OrderStatus;
import com.netcrackerg4.marketplace.repository.interfaces.IDiscountDao;
import com.netcrackerg4.marketplace.repository.interfaces.IProductDao;
import com.netcrackerg4.marketplace.repository.interfaces.IUserDao;
import com.netcrackerg4.marketplace.repository.interfaces.order.IAddressDao;
import com.netcrackerg4.marketplace.repository.interfaces.order.IDeliverySlotDao;
import com.netcrackerg4.marketplace.repository.interfaces.order.IOrderDao;
import com.netcrackerg4.marketplace.service.interfaces.IMailService;
import com.netcrackerg4.marketplace.service.interfaces.IOrderService;
import com.netcrackerg4.marketplace.util.EagerContentPage;
import com.netcrackerg4.marketplace.util.mappers.AddressMapper;
import com.netcrackerg4.marketplace.util.mappers.OrderItemMapper;
import com.netcrackerg4.marketplace.util.mappers.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements IOrderService {
    @Value("${custom.pagination.courier-orders}")
    private final int COURIER_ORDERS;

    private final IDeliverySlotDao deliverySlotDao;
    private final IUserDao userDao;
    private final IOrderDao orderDao;
    private final IProductDao productDao;
    private final IDiscountDao discountDao;
    private final IAddressDao addressDao;
    private final IMailService mailService;

    @Override
    public List<StatusTimestampDto> getDayTimeslots(LocalDate date) {
        Collection<TimeslotEntity> timeslots = deliverySlotDao.readTimeslotOptions();
        int activeCouriers = deliverySlotDao.countActiveCouriers();
        Map<LocalTime, Integer> takenSlots = deliverySlotDao.readTakenTimeslots(date);

        return timeslots.stream().map(ts ->
                new StatusTimestampDto(ts.getTimeStart().toLocalTime(), ts.getTimeEnd().toLocalTime(),
                        activeCouriers - takenSlots.getOrDefault(ts.getTimeStart().toLocalTime(), 0) == 0))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    // fixme: synchronized is temporary
    public synchronized void makeOrder(OrderRequest orderRequest, AppUserEntity maybeCustomer) {
        TimeslotEntity timeslot = deliverySlotDao.readTimeslotOptions().stream()
                .filter(slot -> slot.getTimeStart().toLocalTime().equals(orderRequest.getDeliverySlot().toLocalTime()))
                .findFirst().orElseThrow(() -> new IllegalStateException("Illegal timeslot selected"));
        if (!deliverySlotDao.deliverySlotIsFree(Date.valueOf(orderRequest.getDeliverySlot().toLocalDate()),
                Time.valueOf(orderRequest.getDeliverySlot().toLocalTime())))
            throw new IllegalStateException("This timeslot is already taken");

        Map<UUID, ProductEntity> loadedProducts = handleStocks(orderRequest.getProducts());

        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(orderRequest, orderEntity);
        orderEntity.setOrderId(UUID.randomUUID());
        orderEntity.setPlacedAt(Timestamp.from(Instant.now()));
        orderEntity.setStatus(OrderStatus.SUBMITTED);

        AddressEntity address = new AddressEntity();
        BeanUtils.copyProperties(orderRequest.getAddress(), address);
        address.setAddressId(UUID.randomUUID());
        if (maybeCustomer != null) orderEntity.setCustomer(maybeCustomer);

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

        DeliverySlotEntity deliverySlot = DeliverySlotEntity.builder()
                .deliverySlotId(UUID.randomUUID())
                .datestamp(Date.valueOf(orderRequest.getDeliverySlot().toLocalDate()))
                .timeslotEntity(timeslot)
                .order(orderEntity)
                .courier(deliverySlotDao.findFreeCourier(orderRequest.getDeliverySlot()).orElseThrow())
                .build();
        deliverySlotDao.create(deliverySlot);

        DeliveryDetails deliveryDetails = new DeliveryDetails();
        BeanUtils.copyProperties(orderRequest, deliveryDetails);
        deliveryDetails.setDatestamp(orderRequest.getDeliverySlot().toLocalDate());
        deliveryDetails.setTimeStart(deliverySlot.getTimeslotEntity().getTimeStart().toLocalTime());
        deliveryDetails.setTimeEnd(deliverySlot.getTimeslotEntity().getTimeEnd().toLocalTime());
        deliveryDetails.setCustomerFirstName(maybeCustomer != null ? maybeCustomer.getFirstName() : orderEntity.getFirstName());
        deliveryDetails.setCustomerLastName(maybeCustomer != null ? maybeCustomer.getLastName() : orderEntity.getLastName());
        deliveryDetails.setOrderItems(orderEntity.getOrderItems());

        mailService.notifyCourierGotDelivery(deliverySlot.getCourier().getEmail(), deliveryDetails);
    }

    private Map<UUID, ProductEntity> handleStocks(Collection<OrderItemRequest> orderItems) {
        Map<UUID, ProductEntity> productEntityMap = new HashMap<>(orderItems.size());
        for (OrderItemRequest item : orderItems) {
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
        throw new UnsupportedOperationException();
    }

    @Override
    public EagerContentPage<OrderResponse> getCourierOrders(UUID courierId, List<OrderStatus> targetOrderStatuses, int page) {
        List<OrderEntity> orders = orderDao.readCourierOrders(courierId, targetOrderStatuses, COURIER_ORDERS, page);
        List<OrderResponse> orderDetailsItems = orders.stream().map(order -> {
            OrderResponse orderDetails = new OrderResponse();
            BeanUtils.copyProperties(order, orderDetails);
            orderDetails.setAddress(AddressMapper.entityToDto(order.getAddress()));
            if (order.getCustomer() != null)
                orderDetails.setCustomer(UserMapper.entityToCoreView(order.getCustomer()));
            orderDetails.setDateTimeSlot(deliverySlotDao.findSlotByOrder(orderDetails.getOrderId()).orElseThrow());
            orderDetails.setOrderItems(order.getOrderItems().stream().map(OrderItemMapper::entityToResponse).collect(Collectors.toList()));
            return orderDetails;
        }).collect(Collectors.toList());
        int totalNumOrders = orderDao.countCourierOrders(courierId, targetOrderStatuses);
        int numPages = (int) Math.ceil((double) totalNumOrders / COURIER_ORDERS);
        return new EagerContentPage<>(orderDetailsItems, numPages, COURIER_ORDERS);
    }
}
