package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.domain.user.AppUserEntity;
import com.netcrackerg4.marketplace.model.dto.order.OrderRequest;
import com.netcrackerg4.marketplace.model.dto.order.OrderResponse;
import com.netcrackerg4.marketplace.model.dto.timestamp.StatusTimestampDto;
import com.netcrackerg4.marketplace.service.interfaces.IOrderService;
import com.netcrackerg4.marketplace.service.interfaces.IUserService;
import com.netcrackerg4.marketplace.util.EagerContentPage;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    List<StatusTimestampDto> getTimeslotsInfo(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return orderService.getDayTimeslots(date);
    }

    @PostMapping
    void makeOrder(@RequestBody @Valid OrderRequest orderRequest, Principal principal) {
        AppUserEntity customer = null;
        if (principal != null) {
            customer = userService.findByEmail(principal.getName()).orElse(null);
        }
        orderService.makeOrder(orderRequest, customer);
    }

    @GetMapping
    EagerContentPage<OrderResponse> getCourierOrders(@RequestParam UUID courierId) {
        return orderService.getCourierOrders(courierId);
    }

}
