package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.dto.order.OrderResponse;

import com.netcrackerg4.marketplace.model.response.CourierOrderResponse;
import com.netcrackerg4.marketplace.service.interfaces.ICourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Future;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/courier")
@RequiredArgsConstructor
public class CourierController {

private final ICourierService courierService ;

    @GetMapping("{date}")
    List<CourierOrderResponse> getCourierOrders(@PathVariable @Future(message = "you can only see orders for a future date")
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return courierService.getDayTimeslots(date,email);
    }


}
