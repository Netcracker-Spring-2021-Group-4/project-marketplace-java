package com.netcrackerg4.marketplace.repository.impl;

import com.netcrackerg4.marketplace.config.postgres_queries.order.OrderQueries;
import com.netcrackerg4.marketplace.model.response.CourierDeliveryResponse;
import com.netcrackerg4.marketplace.repository.interfaces.ICourierDao;
import com.netcrackerg4.marketplace.repository.mapper.CourierDeliveryMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public class CourierDaoImpl extends JdbcDaoSupport implements ICourierDao {
    private final OrderQueries orderQueries;

    public CourierDaoImpl(DataSource ds, OrderQueries orderQueries) {
        this.orderQueries = orderQueries;
        super.setDataSource(ds);

    }

    @Override
    public List<CourierDeliveryResponse> getCourierSlots(UUID id, LocalDate date) {

        MapSqlParameterSource namedParams = new MapSqlParameterSource() {{
            addValue("courier_id", id);
            addValue("date", date);
        }};

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
        return namedParameterJdbcTemplate.query(orderQueries.getCourierOrders(),namedParams, new CourierDeliveryMapper());
    }
}
