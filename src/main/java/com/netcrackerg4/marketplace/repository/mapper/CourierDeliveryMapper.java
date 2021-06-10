package com.netcrackerg4.marketplace.repository.mapper;

import com.netcrackerg4.marketplace.model.enums.OrderStatus;
import com.netcrackerg4.marketplace.model.response.CourierDeliveryResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


public  class CourierDeliveryMapper implements RowMapper<CourierDeliveryResponse> {
    @Override
    public CourierDeliveryResponse mapRow(ResultSet rs, int i) throws SQLException {
        return CourierDeliveryResponse.builder()
                .orderId(UUID.fromString(rs.getString("order_id")))
                .timeStart(rs.getTime("time_start"))
                .timeEnd(rs.getTime("time_end"))
                .phoneNumber(rs.getString("phone_number"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .city(rs.getString("city"))
                .street(rs.getString("street"))
                .building(rs.getString("building"))
                .flat(rs.getInt("flat"))
                .statusName(OrderStatus.valueOf(rs.getString("status_name")))
                .build();
                }
                }