package com.netcrackerg4.marketplace.repository.impl;

import com.netcrackerg4.marketplace.config.postgres_queries.DiscountQueries;
import com.netcrackerg4.marketplace.model.domain.DiscountEntity;
import com.netcrackerg4.marketplace.repository.interfaces.IDiscountDao;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Repository
public class DiscountDaoImpl extends JdbcDaoSupport implements IDiscountDao {
    private final DiscountQueries discountQueries;

    @Autowired
    public void setParentDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @Override
    public Optional<DiscountEntity> findActiveProductDiscount(UUID id) {
        Optional<DiscountEntity> discount;
        try {
            discount = Optional.ofNullable(
                    getJdbcTemplate().queryForObject(discountQueries.getFindActiveProductDiscount(),
                            (rs, row) -> DiscountEntity.builder()
                                    .discountId(UUID.fromString(rs.getString("discount_id")))
                                    .offeredPrice(rs.getInt("offered_price"))
                                    .startsAt(rs.getTimestamp("starts_at"))
                                    .endsAt(rs.getTimestamp("ends_at"))
                                    .productId(id)
                                    .build()
                            , id)
            );
        } catch (EmptyResultDataAccessException e) {
            discount = Optional.empty();
        }

        return discount;
    }

    @Override
    public List<DiscountEntity> findUnexpiredDiscounts(UUID productId) {
        try {
            return getJdbcTemplate().query(discountQueries.getFindUnexpiredDiscounts(), (rs, row) ->
                            DiscountEntity.builder()
                                    .discountId(rs.getObject("discount_id", UUID.class))
                                    .offeredPrice(rs.getInt("offered_price"))
                                    .startsAt(rs.getTimestamp("starts_at"))
                                    .endsAt(rs.getTimestamp("ends_at"))
                                    .productId(productId)
                                    .build(),
                    productId);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public void create(DiscountEntity item) {
        Integer overlap = getJdbcTemplate().queryForObject(discountQueries.getCheckPeriod(), Integer.class,
                item.getProductId(), item.getEndsAt(), item.getStartsAt());
        if (overlap != null && overlap > 0)
            throw new IllegalStateException("Discount for this time interval already exists.");
        getJdbcTemplate().update(discountQueries.getCreateDiscount(), item.getDiscountId(),
                item.getOfferedPrice(), item.getStartsAt(), item.getEndsAt(), item.getProductId());
    }

    @Override
    public Optional<DiscountEntity> read(UUID key) {
        try {
            return Optional.ofNullable(getJdbcTemplate().queryForObject(discountQueries.getReadDiscount(), (rs, row) ->
                            DiscountEntity.builder()
                                    .discountId(rs.getObject("discount_id", UUID.class))
                                    .offeredPrice(rs.getInt("offered_price"))
                                    .startsAt(rs.getTimestamp("starts_at"))
                                    .endsAt(rs.getTimestamp("ends_at"))
                                    .productId(rs.getObject("product_id", UUID.class))
                                    .build(),
                    key));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(DiscountEntity updItem) {
        Integer overlap = getJdbcTemplate().queryForObject(discountQueries.getCheckPeriodEdit(), Integer.class,
                updItem.getProductId(), updItem.getDiscountId(), updItem.getEndsAt(), updItem.getStartsAt());
        if (overlap != null && overlap > 0) throw new IllegalStateException("Clashes with another discount.");
        getJdbcTemplate().update(discountQueries.getUpdateDiscount(),
                updItem.getOfferedPrice(), updItem.getStartsAt(), updItem.getEndsAt(), updItem.getDiscountId());
    }

    @Override
    public void delete(UUID key) {
        getJdbcTemplate().update(discountQueries.getDeleteDiscount(), key);
    }
}
