package com.netcrackerg4.marketplace.repository.impl;

import com.netcrackerg4.marketplace.config.postgres_queries.ProductQueries;
import com.netcrackerg4.marketplace.model.domain.AppProductEntity;
import com.netcrackerg4.marketplace.repository.interfaces.IProductDao;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;


import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class ProductDaoImpl extends JdbcDaoSupport implements IProductDao {

    private final ProductQueries productQueries;

    @Autowired
    public void setParentDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @Override
    public void create(AppProductEntity item) {
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(productQueries.getCreateProduct(), item.getProductId(), item.getName(),
                item.getImageUrl(),item.getDescription(), item.getPrice(),item.getInStock(),item.getReserved(),
                item.getAvailabilityDate(),item.getIsActive(),
                item.getCategoryId());
    }

    @Override
    public List<AppProductEntity> getAllProducts() {
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().queryForStream(productQueries.getGetAllProducts(),
            (ResultSet rs, int i) -> AppProductEntity.builder()
                .productId(UUID.fromString(rs.getString("product_id")))
                .name(rs.getString("product_name"))
                .description(rs.getString("description"))
                .imageUrl(rs.getString("image_url"))
                .price(rs.getInt("price"))
                .inStock(rs.getInt("in_stock"))
                .reserved(rs.getInt("reserved"))
                .availabilityDate(rs.getDate("availability_date"))
                .isActive(rs.getBoolean("is_active"))
                .categoryId(rs.getInt("category_id"))
                .build()).collect(Collectors.toList());
    }

    @Override
    public Optional<AppProductEntity> read(UUID key) {
        assert getJdbcTemplate() != null;
        Optional<AppProductEntity> product;
        try {
            product = Optional.ofNullable(
                    getJdbcTemplate().queryForObject(productQueries.getFindProductById(),
                        (rs, rowNum) -> AppProductEntity.builder()
                                .productId(UUID.fromString(rs.getString("product_id")))
                                .name(rs.getString("product_name"))
                                .description(rs.getString("description"))
                                .imageUrl(rs.getString("image_url"))
                                .price(rs.getInt("price"))
                                .inStock(rs.getInt("in_stock"))
                                .reserved(rs.getInt("reserved"))
                                .availabilityDate(rs.getDate("availability_date"))
                                .isActive(rs.getBoolean("is_active"))
                                .categoryId(rs.getInt("category_id"))
                                .build()
                    , key)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return product;
    }

    @Override
    public void update(AppProductEntity updItem) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(UUID key) {
        throw new UnsupportedOperationException();
    }


    @Override
    public void deactivateProduct(UUID productId) {
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(productQueries.getDeactivateProduct(), productId);
    }



}
