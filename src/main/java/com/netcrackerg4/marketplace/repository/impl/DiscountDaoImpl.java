package com.netcrackerg4.marketplace.repository.impl;

import com.netcrackerg4.marketplace.config.postgres_queries.DiscountQueries;
import com.netcrackerg4.marketplace.model.domain.DiscountEntity;
import com.netcrackerg4.marketplace.repository.interfaces.IDiscountDao;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class DiscountDaoImpl extends JdbcDaoSupport implements IDiscountDao {
    private final DiscountQueries discountQueries;

    @Override
    public Optional<DiscountEntity> findActiveProductDiscount(UUID id) {
        assert getJdbcTemplate() != null;
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
    public List<DiscountEntity> readUnexpiredDiscounts(UUID productId) {
        return null;
    }

    @Override
    public void create(DiscountEntity item) {

    }

    @Override
    public Optional<DiscountEntity> read(UUID key) {
        return Optional.empty();
    }

    @Override
    public void update(DiscountEntity updItem) {

    }

    @Override
    public void delete(UUID key) {

    }
}
