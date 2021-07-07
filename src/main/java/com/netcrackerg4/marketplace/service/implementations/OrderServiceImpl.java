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
import com.netcrackerg4.marketplace.model.dto.timestamp.DateTimeSlot;
import com.netcrackerg4.marketplace.model.dto.timestamp.StatusTimestampDto;
import com.netcrackerg4.marketplace.model.enums.OrderStatus;
import com.netcrackerg4.marketplace.model.response.CustomerOrderResponse;
import com.netcrackerg4.marketplace.model.response.OrderInfoResponse;
import com.netcrackerg4.marketplace.model.response.ОrderProductInfo;
import com.netcrackerg4.marketplace.repository.interfaces.IAdvLockUtil;
import com.netcrackerg4.marketplace.repository.interfaces.ICartItemDao;
import com.netcrackerg4.marketplace.repository.interfaces.IDiscountDao;
import com.netcrackerg4.marketplace.repository.interfaces.IProductDao;
import com.netcrackerg4.marketplace.repository.interfaces.order.IAddressDao;
import com.netcrackerg4.marketplace.repository.interfaces.order.IDeliverySlotDao;
import com.netcrackerg4.marketplace.repository.interfaces.order.IOrderDao;
import com.netcrackerg4.marketplace.repository.interfaces.order.IOrderItemDao;
import com.netcrackerg4.marketplace.service.interfaces.IMailService;
import com.netcrackerg4.marketplace.service.interfaces.IOrderService;
import com.netcrackerg4.marketplace.service.interfaces.IOrderStatusAutoUpdate;
import com.netcrackerg4.marketplace.service.interfaces.IProductService;
import com.netcrackerg4.marketplace.util.AdvLockIdUtil;
import com.netcrackerg4.marketplace.util.EagerContentPage;
import com.netcrackerg4.marketplace.util.mappers.AddressMapper;
import com.netcrackerg4.marketplace.util.mappers.OrderEntity_CustomerResponse;
import com.netcrackerg4.marketplace.util.mappers.OrderItemMapper;
import com.netcrackerg4.marketplace.util.mappers.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements IOrderService {
    @Value("${custom.pagination.courier-orders}")
    private final int COURIER_ORDERS;

    private final IDeliverySlotDao deliverySlotDao;
    private final ICartItemDao cartItemDao;
    private final IOrderItemDao orderItemDao;
    private final IOrderDao orderDao;
    private final IProductDao productDao;
    private final IDiscountDao discountDao;
    private final IAddressDao addressDao;
    private final IMailService mailService;
    private final IAdvLockUtil advLockUtil;
    private final IOrderStatusAutoUpdate orderAutoUpdate;
    private final IProductService productService;

    @PostConstruct
    private void initAutoUpdate() {
        orderAutoUpdate.initSchedulers(orderAutoUpdate::updateSubmitted);
    }

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
    public void makeOrder(OrderRequest orderRequest, AppUserEntity maybeCustomer) {
        TimeslotEntity timeslot = deliverySlotDao.readTimeslotOptions().stream()
                .filter(slot -> slot.getTimeStart().toLocalTime().equals(orderRequest.getDeliverySlot().toLocalTime()))
                .findFirst().orElseThrow(() -> new IllegalStateException("Illegal timeslot selected"));
        advLockUtil.requestTransactionLock(AdvLockIdUtil.toLong(orderRequest.getDeliverySlot().toLocalDate().hashCode(),
                orderRequest.getDeliverySlot().toLocalTime().hashCode()));
        AppUserEntity courier = deliverySlotDao.findFreeCourier(orderRequest.getDeliverySlot())
                .orElseThrow(() -> new IllegalStateException("This timeslot is already taken"));

        Map<UUID, ProductEntity> loadedProducts = handleStocksOrdered(orderRequest.getProducts());

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
        if (maybeCustomer != null) cartItemDao.resetCustomerCart(maybeCustomer.getUserId());

        DeliverySlotEntity deliverySlot = DeliverySlotEntity.builder()
                .deliverySlotId(UUID.randomUUID())
                .datestamp(Date.valueOf(orderRequest.getDeliverySlot().toLocalDate()))
                .timeslotEntity(timeslot)
                .order(orderEntity)
                .courier(courier)
                .build();
        deliverySlotDao.create(deliverySlot);

        DeliveryDetails deliveryDetails = new DeliveryDetails();
        BeanUtils.copyProperties(orderRequest, deliveryDetails);
        deliveryDetails.setDatestamp(orderRequest.getDeliverySlot().toLocalDate());
        deliveryDetails.setTimeStart(deliverySlot.getTimeslotEntity().getTimeStart().toLocalTime());
        deliveryDetails.setTimeEnd(deliverySlot.getTimeslotEntity().getTimeEnd().toLocalTime());
        deliveryDetails.setCustomerFirstName(orderEntity.getFirstName());
        deliveryDetails.setCustomerLastName(orderEntity.getLastName());
        deliveryDetails.setOrderItems(orderEntity.getOrderItems());

        mailService.notifyCourierGotDelivery(deliverySlot.getCourier().getEmail(), deliveryDetails);
    }

    @Override
    @Transactional
    public void setOrderStatus(UUID orderId, OrderStatus newStatus) {
        OrderEntity order = orderDao.read(orderId).orElseThrow();

        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.FAILED) {
            throw new IllegalStateException("Status of cancelled or failed order cannot be changed.");
        }
        orderDao.updateStatus(orderId, newStatus);
        if (newStatus == OrderStatus.CANCELLED || newStatus == OrderStatus.FAILED) {
            handleStocksReturn(order.getOrderItems());
        }
    }

    private void handleStocksReturn(Collection<OrderItemEntity> orderItems) {
        orderItems.stream().sorted(Comparator.comparing(OrderItemEntity::getProductId))
                .forEach(item -> advLockUtil.requestTransactionLock(item.getProductId().getMostSignificantBits()));

        for (OrderItemEntity item : orderItems) {
            ProductEntity product = productDao.read(item.getProductId()).orElseThrow();
            product.setInStock(product.getInStock() + item.getQuantity());
            productDao.update(product);
        }
    }

    @Override
    public boolean courierOwnsOrder(UUID userId, UUID orderId) {
        return orderDao.isAssignedToCourier(orderId, userId);
    }

    private int getProductPrice(UUID productId, Map<UUID, ProductEntity> productEntityMap) {
        Optional<DiscountEntity> maybeDiscount = discountDao.findActiveProductDiscount(productId);
        return maybeDiscount.map(DiscountEntity::getOfferedPrice)
                .orElseGet(() -> productEntityMap.get(productId).getPrice());
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

    @Override
    public OrderInfoResponse getOrderDetail(UUID orderId) {
        List<OrderItemEntity> orderItems = orderItemDao.readItemsOfOrder(orderId);
        List<ОrderProductInfo> mapperResults = orderItems.stream()
                .map(this::orderedProductInfoMapper).collect(Collectors.toList());
        int summaryPrice = mapperResults.stream().mapToInt(ОrderProductInfo::getTotalPrice).sum();
        OrderEntity order = orderDao.read(orderId).orElseThrow();
        return OrderInfoResponse.builder()
                .orderId(orderId)
                .placedAt(order.getPlacedAt())
                .phoneNumber(order.getPhoneNumber())
                .firstName(order.getFirstName())
                .lastName(order.getLastName())
                .city(order.getAddress().getCity())
                .street(order.getAddress().getStreet())
                .building(order.getAddress().getBuilding())
                .flat(order.getAddress().getFlat())
                .statusName(order.getStatus())
                .content(mapperResults)
                .summaryPrice(summaryPrice)
                .comment(order.getComment())
                .build();
    }

    private ОrderProductInfo orderedProductInfoMapper(OrderItemEntity orderItem) {
        UUID productId = orderItem.getProductId();
        int quantity = orderItem.getQuantity();
        var product = productService.findProductById(productId).orElseThrow();
        return ОrderProductInfo.builder()
                .productId(productId)
                .name(product.getName())
                .imageUrl(product.getImageUrl())
                .price(orderItem.getPricePerProduct())
                .quantity(quantity)
                .totalPrice(orderItem.getPricePerProduct() * quantity)
                .build();
    }

    @Override
    public List<CustomerOrderResponse> getCustomerOrders(AppUserEntity customer, List<OrderStatus> targetOrderStatuses) {
        List<OrderEntity> orders = orderDao.readCustomerOrders(customer.getUserId(), targetOrderStatuses);
        List<CustomerOrderResponse> ordersResp = orders.stream()
                .map(OrderEntity_CustomerResponse::toOrderResponse)
                .collect(Collectors.toList());
        ordersResp.forEach(order -> {
            DateTimeSlot slot = deliverySlotDao.findSlotByOrder(order.getOrderId()).orElseThrow();
            order.setDeliveryDate(slot.getDeliveryDate().atStartOfDay().toEpochSecond(ZoneOffset.ofHours(3)) * 1000);
            order.setTimeStart(LocalDate.now().atTime(slot.getTimeStart()).toEpochSecond(ZoneOffset.ofHours(3)) * 1000);
            order.setTimeEnd(LocalDate.now().atTime(slot.getTimeEnd()).toEpochSecond(ZoneOffset.ofHours(3)) * 1000);
        });
        return ordersResp;
    }

    @Override
    public boolean customerOwnsOrder(UUID userId, UUID orderId) {
        return orderDao.isMadeByCustomer(orderId, userId);
    }

    private Map<UUID, ProductEntity> handleStocksOrdered(Collection<OrderItemRequest> orderItems) {
        Map<UUID, ProductEntity> productEntityMap = new HashMap<>(orderItems.size());
        orderItems.stream().sorted(Comparator.comparing(OrderItemRequest::getProductId))
                .forEach(item -> advLockUtil.requestTransactionLock(item.getProductId().getMostSignificantBits()));

        for (OrderItemRequest item : orderItems) {
            ProductEntity product = productDao.read(item.getProductId()).orElseThrow();
            productEntityMap.put(product.getProductId(), product);
            if (product.getReserved() < item.getQuantity()) {
                throw new IllegalStateException("There should have been more products reserved.");
            }
            if (!product.getIsActive()) {
                throw new IllegalStateException("Only active products can be ordered.");
            }
            product.setReserved(product.getReserved() - item.getQuantity());
            product.setInStock(product.getInStock() - item.getQuantity());
            productDao.update(product);
        }
        return productEntityMap;
    }
}
