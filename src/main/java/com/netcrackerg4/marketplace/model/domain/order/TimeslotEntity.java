package com.netcrackerg4.marketplace.model.domain.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeslotEntity {
    private int timeslotId;
    private Time timeStart;
    private Time timeEnd;
}
