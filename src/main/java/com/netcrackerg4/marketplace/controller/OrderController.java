package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.domain.user.AppUserEntity;
import com.netcrackerg4.marketplace.model.dto.order.OrderRequest;
import com.netcrackerg4.marketplace.model.dto.timestamp.StatusTimestampDto;
import com.netcrackerg4.marketplace.model.enums.UserRole;
import com.netcrackerg4.marketplace.model.response.OrderInfoResponse;
import com.netcrackerg4.marketplace.service.interfaces.IOrderService;
import com.netcrackerg4.marketplace.service.interfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/public/orders")
@AllArgsConstructor
public class OrderController {
    private final IOrderService orderService;
    private final IUserService userService;

    @GetMapping("/timeslots/{date}")
    List<StatusTimestampDto> getTimeslotsInfo(@PathVariable @Future(message = "you can only make an order for a future date")
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return orderService.getDayTimeslots(date);
    }

    @GetMapping("/{orderId}")
    public OrderInfoResponse getOrderDetails(@PathVariable UUID orderId,
                                                                      @Nullable Principal principal) {
        if (principal == null) throw new IllegalStateException("Unauthorized access is forbidden.");
        AppUserEntity user = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Could not find such user."));
        // product managers and admin can see all orders
        switch (user.getRole()) {
            case ROLE_CUSTOMER:
                if (!orderService.customerOwnsOrder(user.getUserId(), orderId)){
                    throw new IllegalStateException("You can only see your own orders.");
                }
                break;
            case ROLE_COURIER:
                if (!orderService.courierOwnsOrder(user.getUserId(), orderId)){
                    throw new IllegalStateException("You can only see orders assigned to you.");
                }
                break;
        }
        return orderService.getOrderDetail(orderId);
    }

    @PostMapping
    void makeOrder(@RequestBody @Valid OrderRequest orderRequest, Principal principal) {
        AppUserEntity customer = null;
        if (principal != null) {
            customer = userService.findByEmail(principal.getName()).orElse(null);
            if (customer != null && customer.getRole() != UserRole.ROLE_CUSTOMER)
                throw new IllegalStateException("Only customers can make orders.");
        } else if (orderRequest.getFirstName() == null || orderRequest.getLastName() == null)
            throw new IllegalStateException("Customer must provide his full name.");
        orderService.makeOrder(orderRequest, customer);
    }
}