package com.netcrackerg4.marketplace.model.dto.product.mapper;

import com.netcrackerg4.marketplace.model.dto.product.ProductDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ProductRowMapper implements RowMapper<ProductDto> {

        public ProductDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductDto product = new ProductDto();
            product.setProductId(UUID.fromString(rs.getString("product_id")));
            product.setProductName(rs.getString("product_name"));
            product.setDescription(rs.getString("description"));
            product.setPrice(rs.getDouble("price"));
            product.setInStock(rs.getInt("in_stock"));
            product.setReserved(rs.getInt("reserved"));
            product.setAvailabilityDate(rs.getDate("availability_date").toLocalDate());
            product.setActive(rs.getBoolean("is_active"));
            product.setCategoryId(rs.getInt("category_id"));
            return product;
        }
    }


