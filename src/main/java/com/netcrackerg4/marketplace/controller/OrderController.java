package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.dto.timestamp.StatusTimestampDto;
import com.netcrackerg4.marketplace.service.interfaces.IOrderService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/public/orders")
@AllArgsConstructor
public class OrderController {
    private final IOrderService orderService;

    @GetMapping("/timeslots/{date}")
    List<StatusTimestampDto> getTimeslotsInfo(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return orderService.getDayTimeslots(date);
    }
}
