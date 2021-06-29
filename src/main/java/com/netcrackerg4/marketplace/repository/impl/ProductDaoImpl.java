package com.netcrackerg4.marketplace.repository.impl;

import com.netcrackerg4.marketplace.config.postgres_queries.ProductQueries;
import com.netcrackerg4.marketplace.model.domain.product.ProductEntity;
import com.netcrackerg4.marketplace.model.domain.product.RecommendationEntity;
import com.netcrackerg4.marketplace.model.enums.SortingOptions;
import com.netcrackerg4.marketplace.model.response.ProductResponse;
import com.netcrackerg4.marketplace.repository.interfaces.IProductDao;
import com.netcrackerg4.marketplace.repository.mapper.ProductResponseMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
            case DATE:
                sqlQuery=productQueries.getProductsWithFiltersOrderByDate();
                break;
            default:
                sqlQuery=productQueries.getProductsWithFiltersOrderByPopularity();
        }

        return namedParameterJdbcTemplate. query(sqlQuery,
                namedParams, new ProductResponseMapper()
        );
    }

    @Override
    public int findAllSize() {
        Integer numFound= getJdbcTemplate().queryForObject(productQueries.getActiveProductsSize(),Integer.class);
        return numFound!=null?numFound:0;
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

        Integer numFound = namedParameterJdbcTemplate.queryForObject(productQueries.getActiveProductsFilteredSize(),
                namedParams, Integer.class);

        return numFound!=null?numFound:0;

    }

    @Override
    public void activateDeactivateProduct(ProductEntity product) {
        getJdbcTemplate().update(productQueries.getActivateDeactivateProduct(), product.getAvailabilityDate(), product.getReserved(), product.getProductId());
}

    public Optional<ProductResponse> findProductForComparison(UUID id) {
        Optional<ProductResponse> product;
        try {
            product = Optional.ofNullable(
                    getJdbcTemplate().queryForObject(productQueries.getFindProductById(),
                            new ProductResponseMapper(true, false, false)
                            , id)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return product;
    }

    @Override
    public List<UUID> popularNowIds(int limit) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource() {{
            addValue("limit", limit);
        }};

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());

        return namedParameterJdbcTemplate.queryForList(productQueries.getPopularNow(),
                namedParams, UUID.class
        );
    }

    @Override
    public void clearPopularNow() {
        getJdbcTemplate().update(productQueries.getClearPopularNow());
    }

    @Override
    public void updatePopularNow(List<UUID> ids) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource() {{
            addValue("ids", ids);
        }};

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());

        namedParameterJdbcTemplate.update(productQueries.getUpdatePopularNow(),
                namedParams  );

    }

    @Override
    public int getProductsSupport(UUID productX, UUID productY) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource() {{
            addValue("prodX", productX);
            addValue("prodY", productY);
        }};

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());

        Integer support= namedParameterJdbcTemplate.queryForObject(productQueries.getProductsSupport(),
                namedParams, Integer.class);
        return support!=null?support:0;
    }

    @Transactional
    @Override
    public void updateRecommendations(List<RecommendationEntity> recommends) {
        if(recommends.isEmpty())
            throw new IllegalStateException("THERE is NOTHING TO UPDATE");

        getJdbcTemplate().update(productQueries.getDeleteRecommendations());
        getJdbcTemplate().batchUpdate(productQueries.getInsertRecommendation(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, recommends.get(i).getProductA());
                ps.setObject(2, recommends.get(i).getProductB());
                ps.setDouble(3, recommends.get(i).getLift());
            }

            @Override
            public int getBatchSize() {
                return recommends.size();
            }
        });

    }

    @Override
    public List<ProductResponse> usuallyBuyThisProductWith(UUID productId, int amount) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource() {{
            addValue("product_id", productId);
            addValue("limit", amount);
        }};

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());

        return namedParameterJdbcTemplate.query(productQueries.getProductRecommendations(), namedParams, new ProductResponseMapper());
    }


    @Override
    public Map<UUID, Integer> getAllProductsSupport() {

       return  getJdbcTemplate().query(productQueries.getAllProductSupport(), (ResultSet rs) -> {
            Map<UUID,Integer> results = new HashMap<>();
            while (rs.next()) {
                results.put(UUID.fromString(rs.getString("product_id")), rs.getInt("count"));
            }
            return results;});
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
