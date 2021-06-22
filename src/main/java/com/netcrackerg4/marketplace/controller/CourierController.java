package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.domain.user.AppUserEntity;
import com.netcrackerg4.marketplace.model.dto.order.OrderResponse;
import com.netcrackerg4.marketplace.model.enums.OrderStatus;
import com.netcrackerg4.marketplace.model.response.CourierDeliveryResponse;
import com.netcrackerg4.marketplace.service.interfaces.ICartService;
import com.netcrackerg4.marketplace.service.interfaces.ICourierService;
import com.netcrackerg4.marketplace.service.interfaces.IOrderService;
import com.netcrackerg4.marketplace.service.interfaces.IUserService;
import com.netcrackerg4.marketplace.util.EagerContentPage;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/courier")
@RequiredArgsConstructor
public class CourierController {

    private final ICourierService courierService;
    private final IOrderService orderService;
    private final IUserService userService;
    private final ICartService cartService;

    @GetMapping("{date}")
    List<CourierDeliveryResponse> getCourierOrders(@PathVariable
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                           LocalDate date) {
        if (date.isBefore(LocalDate.now()))
            throw new IllegalStateException("You can not see your past deliveries");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return courierService.getDayTimeslots(date, email);
    }

    @GetMapping("/orders")
    EagerContentPage<OrderResponse> getCourierOrders(@RequestParam @Min(0) int page, Principal principal) {
        UUID courierId = userService.findByEmail(principal.getName()).orElseThrow().getUserId();
        return orderService.getCourierOrders(courierId, List.of(OrderStatus.SUBMITTED, OrderStatus.IN_DELIVERY), page);
    }

    @PatchMapping("/orders/{orderId}")
    void updateOrderState(@PathVariable UUID orderId, @RequestParam OrderStatus orderStatus, Principal principal) {
        AppUserEntity courier = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Could not find such courier."));
        if (!orderService.courierOwnsOrder(courier.getUserId(), orderId))
            throw new IllegalStateException("you are not assigned to this delivery");
        orderService.setOrderStatus(orderId, orderStatus, false);
    }

}