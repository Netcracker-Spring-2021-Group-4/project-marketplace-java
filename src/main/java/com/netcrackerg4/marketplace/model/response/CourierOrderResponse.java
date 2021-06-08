package com.netcrackerg4.marketplace.model.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.netcrackerg4.marketplace.config.postgres_queries.order.OrderQueries;
import com.netcrackerg4.marketplace.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CourierOrderResponse {
    private UUID orderId;
    private Timestamp placedAt;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String city;
    private String street;
    private String building;
    private int flat;
    private OrderStatus statusName;


    public static class OrderResponseMapper implements RowMapper<CourierOrderResponse> {

    @Override
    public CourierOrderResponse mapRow(ResultSet rs, int i) throws SQLException {
        return CourierOrderResponse.builder()
                .orderId(UUID.fromString(rs.getString("order_id")))
                .placedAt(rs.getTimestamp("placed_at"))
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




}
