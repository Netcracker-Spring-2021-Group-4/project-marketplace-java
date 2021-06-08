package com.netcrackerg4.marketplace.repository.impl;

import com.netcrackerg4.marketplace.config.postgres_queries.ProductQueries;
import com.netcrackerg4.marketplace.model.domain.ProductEntity;
import com.netcrackerg4.marketplace.model.response.ProductResponse;
import com.netcrackerg4.marketplace.model.enums.SortingOptions;
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
        getJdbcTemplate().update(productQueries.getCreateProduct(), item.getProductId(), item.getName(),
                item.getImageUrl(),item.getDescription(), item.getPrice(),item.getInStock(),item.getReserved(),
                item.getAvailabilityDate(),item.getIsActive(),
                item.getCategoryId());
    }

    @Override
    public Optional<ProductEntity> read(UUID key) {
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
        getJdbcTemplate().update(productQueries.getUpdateProductInfo(), updItem.getName(),
                updItem.getDescription(),updItem.getPrice(), updItem.getInStock(),updItem.getReserved(),
                updItem.getCategoryId(),updItem.getProductId());
    }

    @Override
    public void updatePicture(UUID key, URL url) {
        getJdbcTemplate().update(productQueries.getUpdateProductPicture(), url.toString(),key);
    }



    @Override
    public List<ProductResponse> findAll(int p, int s) {
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

      public List<ProductResponse> findProductsWithFilters(String query, List<Integer> categories, int from, int to, SortingOptions sortBy, int pageN, int pageSize) {

        MapSqlParameterSource namedParams = new MapSqlParameterSource() {{
            addValue("category_ids", categories);
            addValue("minPrice", from);
            addValue("maxPrice", to);
            addValue("name_query", "%" + query + "%");
            addValue("limit", pageSize);
            addValue("offset", pageSize * pageN);
        }};

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
        String sqlQuery;
        switch (sortBy){
            case PRICE_ASC:
                sqlQuery=productQueries.getProductsWithFiltersOrderByPriceAsc();
                break;
            case PRICE_DESC:
                sqlQuery=productQueries.getProductsWithFiltersOrderByPriceDesc();
                break;
            case NAME:
                sqlQuery=productQueries.getProductsWithFiltersOrderByName();
                break;
            default:
                sqlQuery=productQueries.getProductsWithFiltersOrderByDate();
        }

        return namedParameterJdbcTemplate. query(sqlQuery,
                namedParams, new ProductResponse.ProductResponseMapper()
        );
    }

    @Override
    public int findAllSize() {
        return getJdbcTemplate().queryForObject(productQueries.getActiveProductsSize(),Integer.class);
    }

    @Override
    public int findAllFilteredSize(String query, List<Integer> categories, int from, int to) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource() {{
            addValue("category_ids", categories);
            addValue("minPrice", from);
            addValue("maxPrice", to);
            addValue("name_query", "%" + query + "%");
        }};

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());

        return namedParameterJdbcTemplate.queryForObject(productQueries.getActiveProductsFilteredSize(),
                namedParams, Integer.class
        );

    }

    @Override
    public void activateDeactivateProduct(ProductEntity product) {
        getJdbcTemplate().update(productQueries.getActivateDeactivateProduct(), product.getAvailabilityDate(), product.getReserved(), product.getProductId());
    }

    @Override
    public Integer maxPrice() {
        return getJdbcTemplate().queryForObject(productQueries.getMaxPrice(),Integer.class);
    }


    @Override
    public void delete(UUID key) {
        throw new UnsupportedOperationException();
    }

}
