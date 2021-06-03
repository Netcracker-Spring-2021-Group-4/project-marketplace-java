package com.netcrackerg4.marketplace.repository.impl;

import com.netcrackerg4.marketplace.config.postgres_queries.ProductQueries;
import com.netcrackerg4.marketplace.model.domain.ProductEntity;
import com.netcrackerg4.marketplace.model.response.ProductResponse;
import com.netcrackerg4.marketplace.repository.interfaces.IProductDao;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class ProductDaoImpl extends JdbcDaoSupport implements IProductDao {

    private final ProductQueries productQueries;

    @Autowired
    public void setParentDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @Override
    public void create(ProductEntity item) {
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(productQueries.getCreateProduct(), item.getProductId(), item.getName(),
                item.getImageUrl(),item.getDescription(), item.getPrice(),item.getInStock(),item.getReserved(),
                item.getAvailabilityDate(),item.getIsActive(),
                item.getCategoryId());
    }

    @Override
    public Optional<ProductEntity> read(UUID key) {
        assert getJdbcTemplate() != null;
        Optional<ProductEntity> product;
        try {
            product = Optional.ofNullable(
                    getJdbcTemplate().queryForObject(productQueries.getFindProductById(),
                        (rs, rowNum) -> ProductEntity.builder()
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
    public void update(ProductEntity updItem) {
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(productQueries.getUpdateProductInfo(), updItem.getName(),
                updItem.getDescription(),updItem.getPrice(), updItem.getInStock(),updItem.getReserved(),
                updItem.getCategoryId(),updItem.getProductId());
    }

    @Override
    public void updatePicture(UUID key, URL url) {
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(productQueries.getUpdateProductPicture(), url.toString(),key);
    }

    @Override
    public List<ProductResponse> findAll() {
        assert getJdbcTemplate() != null;

        return getJdbcTemplate().query(productQueries.getActiveProductsWithDiscount(),
               new ProductResponse.ProductResponseMapper());
    }

    @Override
    public List<ProductResponse> findAll(int p, int s) {
        assert getJdbcTemplate() != null;

        MapSqlParameterSource namedParams = new MapSqlParameterSource() {
            {
                addValue("limit", s);
                addValue("offset", p * s);
            }
        };
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
        return namedParameterJdbcTemplate.query(productQueries.getProductsPage(),
                namedParams, new ProductResponse.ProductResponseMapper()
        );

    }

    @Override
    public Double maxPrice() {
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().queryForObject(productQueries.getMaxPrice(),Double.class);
    }



    public List<ProductResponse> findProductsWithFilters(String query, List<Integer> categories, int from, int to, String sortBy, int pageN, int pageSize) {
        assert getJdbcTemplate() != null;

        MapSqlParameterSource namedParams = new MapSqlParameterSource() {{
            addValue("category_ids", categories);
            addValue("minPrice", from);
            addValue("maxPrice", to);
            addValue("name_query", "%" + query + "%");
            addValue("sortOption",sortBy);
            addValue("limit", pageSize);
            addValue("offset", pageSize * pageN);
        }};

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
        return namedParameterJdbcTemplate.query(productQueries.getProductsWithFilters(),
                namedParams, new ProductResponse.ProductResponseMapper()
        );    }


    @Override
    public void delete(UUID key) {
        throw new UnsupportedOperationException();
    }

}
