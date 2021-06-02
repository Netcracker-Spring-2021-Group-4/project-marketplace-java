package com.netcrackerg4.marketplace.model.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProductResponse {
        private final UUID productId;
        private String name;
        private String description;
        private String imageUrl;
        private int price;
        private int inStock;
        private int reserved;
        private Date availabilityDate;
        private int categoryId;
        private int discount;


        public static class ProductResponseMapper implements RowMapper<ProductResponse> {

        @Override
        public ProductResponse mapRow(ResultSet rs, int i) throws SQLException {
                return ProductResponse.builder()
                        .productId(UUID.fromString(rs.getString("product_id")))
                        .name(rs.getString("product_name"))
                        .description(rs.getString("description"))
                        .imageUrl(rs.getString("image_url"))
                        .price(rs.getInt("price"))
                        .inStock(rs.getInt("in_stock"))
                        .reserved(rs.getInt("reserved"))
                        .availabilityDate(rs.getDate("availability_date"))
                        .categoryId(rs.getInt("category_id"))
                        .discount(rs.getInt("offered_price"))
                        .build();
        }
}


}
