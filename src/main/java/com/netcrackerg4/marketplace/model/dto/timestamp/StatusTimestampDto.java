package com.netcrackerg4.marketplace.model.dto.timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class StatusTimestampDto {
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private boolean isTaken;
}
