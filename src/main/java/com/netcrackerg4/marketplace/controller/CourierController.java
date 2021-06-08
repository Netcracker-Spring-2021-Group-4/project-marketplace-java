package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.response.CourierDeliveryResponse;
import com.netcrackerg4.marketplace.service.interfaces.ICourierService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
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
    List<CourierDeliveryResponse> getCourierOrders(@PathVariable
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                           LocalDate date) {

        if(date.isBefore(LocalDate.now()))
            throw new IllegalStateException("You can not see your past deliveries");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return courierService.getDayTimeslots(date,email);
    }


}
