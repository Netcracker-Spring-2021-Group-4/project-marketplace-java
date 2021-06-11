package com.netcrackerg4.marketplace.model.domain.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Comparator;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeslotEntity {
    private int timeslotId;
    private Time timeStart;
    private Time timeEnd;

    @Override
    public int hashCode() {
        return timeslotId;
    }

    public static Comparator<TimeslotEntity> getStartComparator() {
        return Comparator.comparing(TimeslotEntity::getTimeStart);
    }
}
