package com.netcrackerg4.marketplace.repository.mapper;

import com.netcrackerg4.marketplace.model.response.ProductResponse;
import lombok.Getter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Getter
public class ProductResponseMapper implements RowMapper<ProductResponse> {
    private final boolean fillDescription;
    private final boolean fillInStock;
    private final boolean fillOfferedPrice;
    private final boolean fillPopularNow;

    public ProductResponseMapper() {
        this.fillDescription = true;
        this.fillInStock = true;
        this.fillOfferedPrice = true;
        this.fillPopularNow=true;
    }

    public ProductResponseMapper(boolean fillDescription, boolean fillInStock, boolean fillOfferedPrice) {
        this.fillDescription = fillDescription;
        this.fillInStock = fillInStock;
        this.fillOfferedPrice = fillOfferedPrice;
        this.fillPopularNow=false;
    }
    public ProductResponseMapper(boolean fillDescription, boolean fillInStock, boolean fillOfferedPrice, boolean fillPopularNow) {
        this.fillDescription = fillDescription;
        this.fillInStock = fillInStock;
        this.fillOfferedPrice = fillOfferedPrice;
        this.fillPopularNow = fillPopularNow;
    }


    @Override
    public ProductResponse mapRow(ResultSet rs, int i) throws SQLException {
        var result =  ProductResponse.builder()
                .productId(UUID.fromString(rs.getString("product_id")))
                .name(rs.getString("product_name"))
                .imageUrl(rs.getString("image_url"))
                .price(rs.getInt("price"))
                .categoryId(rs.getInt("category_id"));

        if(fillDescription) result.description(rs.getString("description"));
        if(fillInStock) result.inStock(rs.getInt("in_stock"));
        if(fillOfferedPrice) result.discount(rs.getInt("offered_price"));
        if(fillPopularNow) result.popularNow(rs.getBoolean("popular_now"));
        return result.build();
    }

}
