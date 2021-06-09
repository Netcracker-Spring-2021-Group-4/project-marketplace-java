package com.netcrackerg4.marketplace.repository.mapper;

import com.netcrackerg4.marketplace.model.response.ProductResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ProductResponseFullMapper implements RowMapper<ProductResponse> {

    @Override
    public ProductResponse mapRow(ResultSet rs, int i) throws SQLException {
        return ProductResponse.builder()
                .productId(UUID.fromString(rs.getString("product_id")))
                .name(rs.getString("product_name"))
                .description(rs.getString("description"))
                .imageUrl(rs.getString("image_url"))
                .price(rs.getInt("price"))
                .inStock(rs.getInt("in_stock"))
                .categoryId(rs.getInt("category_id"))
                .discount(rs.getInt("offered_price"))
                .build();
    }

}
