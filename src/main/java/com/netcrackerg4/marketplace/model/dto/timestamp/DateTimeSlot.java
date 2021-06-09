package com.netcrackerg4.marketplace.model.dto.timestamp;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class DateTimeSlot {
    private LocalDate deliveryDate;
    private LocalTime timeStart;
    private LocalTime timeEnd;
}
