package com.netcrackerg4.marketplace.repository.impl.order;

import com.netcrackerg4.marketplace.config.postgres_queries.order.DeliverySlotQueries;
import com.netcrackerg4.marketplace.model.domain.order.TimeslotEntity;
import com.netcrackerg4.marketplace.repository.interfaces.order.IDeliverySlotDao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Repository
public class DeliverySlotDaoImpl extends JdbcDaoSupport implements IDeliverySlotDao {
    private final DeliverySlotQueries deliverySlotQueries;
    private List<TimeslotEntity> timeslots;

    @Autowired
    public void setParentDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @PostConstruct
    private void initTimeslots() {
        assert getJdbcTemplate() != null;
        timeslots = getJdbcTemplate().query(deliverySlotQueries.getReadAllTimeslots(),
                (rs, row) -> new TimeslotEntity(rs.getInt("timeslot_id"),
                        rs.getTime("time_start"), rs.getTime("time_end")));
    }

    @Override
    public List<TimeslotEntity> readTimeslotOptions() {
        return timeslots;
    }

    @Override
    public Map<LocalTime, Integer> readTakenTimeslots(LocalDate date) {
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().queryForStream(deliverySlotQueries.getGetTakenTimeslots(), (rs, row) ->
                        new DeliveryGrouper(rs.getTime("time_start").toLocalTime(), rs.getInt("num_taken")),
                Date.valueOf(date)).collect(Collectors.toMap(DeliveryGrouper::getTime, DeliveryGrouper::getN));
    }

    @Override
    public int countActiveCouriers() {
        assert getJdbcTemplate() != null;
        try {
            Integer n = getJdbcTemplate().queryForObject(deliverySlotQueries.getCountActiveCouriers(), Integer.class);
            return n != null ? n : 0;
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    @AllArgsConstructor
    @Getter
    private static class DeliveryGrouper {
        private final LocalTime time;
        private final int n;
    }
}
