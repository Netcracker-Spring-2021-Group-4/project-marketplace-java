package com.netcrackerg4.marketplace.model.dto.timestamp;

import lombok.Data;

import java.time.LocalTime;

@Data
public class SimpleTimestampDto {
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private boolean isTaken;
}
