package com.netcrackerg4.marketplace.repository.impl;

import com.netcrackerg4.marketplace.config.postgres_queries.ProductQueries;
import com.netcrackerg4.marketplace.model.dto.product.ProductDto;
import com.netcrackerg4.marketplace.model.dto.product.mapper.ProductRowMapper;
import com.netcrackerg4.marketplace.repository.interfaces.IProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public class ProductDaoImpl  implements IProductDao {

    private final JdbcTemplate jdbcTemplate;


    private final ProductQueries queries;

    @Autowired
    public ProductDaoImpl( JdbcTemplate jdbcTemplate, ProductQueries productQueries) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = productQueries;
    }


    @Override
    public void create(ProductDto item) {

        jdbcTemplate.update(queries.getAddProduct(),
                item.getProductId(),
                item.getProductName(),
                item.getDescription(),
                item.getPrice(),
                item.getInStock(),
                item.getReserved(),
                item.getAvailabilityDate(),
                item.isActive(),
                item.getCategoryId());
    }

    @Override
    public ProductDto read(UUID key) {
        return jdbcTemplate.queryForObject(queries.getFindProductById(), new ProductRowMapper(),key);
    }

    @Override
    public void update(ProductDto updItem) {

    }

    @Override
    public void delete(UUID key) {

    }
}
