package com.netcrackerg4.marketplace.repository.impl.order;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.netcrackerg4.marketplace.config.postgres_queries.order.DeliverySlotQueries;
import com.netcrackerg4.marketplace.model.domain.order.DeliverySlotEntity;
import com.netcrackerg4.marketplace.model.domain.order.TimeslotEntity;
import com.netcrackerg4.marketplace.model.domain.user.AppUserEntity;
import com.netcrackerg4.marketplace.model.dto.timestamp.DateTimeSlot;
import com.netcrackerg4.marketplace.model.enums.UserRole;
import com.netcrackerg4.marketplace.model.enums.UserStatus;
import com.netcrackerg4.marketplace.model.response.CourierDeliveryResponse;
import com.netcrackerg4.marketplace.repository.interfaces.order.IDeliverySlotDao;
import com.netcrackerg4.marketplace.repository.mapper.CourierDeliveryMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
@DependsOn("flywayInitializer")
public class DeliverySlotDaoImpl extends JdbcDaoSupport implements IDeliverySlotDao {
    private final DeliverySlotQueries deliverySlotQueries;
    private Set<TimeslotEntity> timeslots;
    private BiMap<Integer, Time> timeslotsIds;

    @Autowired
    public void setParentDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @PostConstruct
    private void initTimeslots() {
        timeslots = new HashSet<>(getJdbcTemplate().query(deliverySlotQueries.getReadAllTimeslots(),
                (rs, row) -> new TimeslotEntity(rs.getInt("timeslot_id"),
                        rs.getTime("time_start"), rs.getTime("time_end"))));
        timeslotsIds = HashBiMap.create(timeslots.size());
        timeslots.forEach(slot -> timeslotsIds.put(slot.getTimeslotId(), slot.getTimeStart()));
    }

    @Override
    public void create(DeliverySlotEntity deliverySlot) {
        getJdbcTemplate().update(deliverySlotQueries.getCreateDeliverySlot(), deliverySlot.getDeliverySlotId(),
                deliverySlot.getDatestamp(), deliverySlot.getTimeslotEntity().getTimeslotId(),
                deliverySlot.getCourier().getUserId(), deliverySlot.getOrder().getOrderId());
    }

    @Override
    public Collection<TimeslotEntity> readTimeslotOptions() {
        return timeslots;
    }

    @Override
    public Map<LocalTime, Integer> readTakenTimeslots(LocalDate date) {
        return getJdbcTemplate().queryForStream(deliverySlotQueries.getReadTakenTimeslots(), (rs, row) ->
                        new DeliveryGrouper(rs.getTime("time_start").toLocalTime(), rs.getInt("num_taken")),
                Date.valueOf(date)).collect(Collectors.toMap(DeliveryGrouper::getTime, DeliveryGrouper::getN));
    }

    @Override
    public int countActiveCouriers() {
        try {
            Integer n = getJdbcTemplate().queryForObject(deliverySlotQueries.getCountActiveCouriers(), Integer.class);
            return n != null ? n : 0;
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    @Override
    public Optional<AppUserEntity> findFreeCourier(LocalDateTime deliveryTimeSlot) {
        try {
            AppUserEntity userEntity = getJdbcTemplate().queryForObject(deliverySlotQueries.getFindFreeCourier(), (rs, row) -> AppUserEntity.builder()
                            .userId(rs.getObject("user_id", UUID.class))
                            .email(rs.getString("email"))
                            .password(rs.getString("password"))
                            .firstName(rs.getString("first_name"))
                            .lastName(rs.getString("last_name"))
                            .phoneNumber(rs.getString("phone_number"))
                            .role(UserRole.ROLE_COURIER)
                            .status(UserStatus.ACTIVE)
                            .build(),
                    Date.valueOf(deliveryTimeSlot.toLocalDate()),
                    timeslotsIds.inverse().get(Time.valueOf(deliveryTimeSlot.toLocalTime())));
            return Optional.ofNullable(userEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public Optional<DateTimeSlot> findSlotByOrder(UUID orderId) {
        try {
            return Optional.ofNullable(getJdbcTemplate().queryForObject(deliverySlotQueries.getFindDeliverySlotByOrderId(), (rs, row) ->
                            DateTimeSlot.builder()
                                    .deliveryDate(rs.getDate("datestamp").toLocalDate())
                                    .timeStart(rs.getTime("time_start").toLocalTime())
                                    .timeEnd(rs.getTime("time_end").toLocalTime()).build(),
                    orderId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<CourierDeliveryResponse> getCourierSlots(UUID id, LocalDate date) {

        MapSqlParameterSource namedParams = new MapSqlParameterSource() {{
            addValue("courier_id", id);
            addValue("date", date);
        }};

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
        return namedParameterJdbcTemplate.query(deliverySlotQueries.getCourierOrders(),namedParams, new CourierDeliveryMapper());
    }

    @AllArgsConstructor
    @Getter
    private static class DeliveryGrouper {
        private final LocalTime time;
        private final int n;
    }
}
