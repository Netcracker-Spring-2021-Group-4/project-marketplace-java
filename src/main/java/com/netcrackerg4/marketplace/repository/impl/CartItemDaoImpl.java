package com.netcrackerg4.marketplace.repository.impl;

import com.netcrackerg4.marketplace.config.postgres_queries.CartQueries;
import com.netcrackerg4.marketplace.model.domain.product.CartItemEntity;
import com.netcrackerg4.marketplace.model.dto.product.CartItemDto;
import com.netcrackerg4.marketplace.repository.interfaces.ICartItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CartItemDaoImpl extends JdbcDaoSupport implements ICartItemDao {

    private final CartQueries cartQueries;

    @Autowired
    public CartItemDaoImpl(DataSource ds, CartQueries cartQueries) {
        this.cartQueries = cartQueries;
        super.setDataSource(ds);
    }

    @Override
    public void addToCart(CartItemEntity item) {
        getJdbcTemplate()
                .update(cartQueries.getAddToCart(),
                        item.getCartItemId(),
                        item.getQuantity(),
                        item.getTimestampAdded(),
                        item.getCustomerId(),
                        item.getProductId());

    }

    @Override
    public void removeFromCart(UUID cartItemId) {
        getJdbcTemplate()
                .update(cartQueries.getRemoveFromCart(),
                        cartItemId);
    }

    @Override
    public Optional<CartItemEntity> getCartItemByProductAndCustomer(UUID customerId, UUID productId) {
        Optional<CartItemEntity> result;
        try {
            result =  Optional.ofNullable(getJdbcTemplate().queryForObject(cartQueries.getGetByCustomerProduct(),
                    (rs, row) -> CartItemEntity.builder()
                            .cartItemId(UUID.fromString(rs.getString("cart_item_id")))
                            .quantity(rs.getInt("quantity"))
                            .timestampAdded(rs.getTimestamp("timestamp_added"))
                            .customerId(UUID.fromString(rs.getString("customer_id")))
                            .customerId(UUID.fromString(rs.getString("product_id")))
                            .build()
                    , customerId, productId));
        } catch (EmptyResultDataAccessException e) {
            result = Optional.empty();
        }

        return result;
    }

    @Override
    public void changeQuantityById(int quantity, UUID cartItemId) {
        getJdbcTemplate()
                .update(cartQueries.getChangeQuantityById(),
                        quantity,
                        cartItemId);
    }

    @Override
    public List<CartItemDto> getAuthCustomerCartItems(UUID id) {
        List<CartItemDto> result;

        result = getJdbcTemplate().query(cartQueries.getFindAuthCustomerCartItems(),
                (rs, row) -> CartItemDto.builder()
                        .productId(UUID.fromString(rs.getString("product_id")))
                        .quantity(rs.getInt("quantity"))
                        .build(), id);

        return result;
    }

    @Override
    public int reserveProduct(int quantity, UUID productId) {
        long lockId = productId.getMostSignificantBits();
        return getJdbcTemplate().update(cartQueries.getReserveProduct(),
                lockId, quantity, productId);
    }

    @Override
    public int cancelReservation(int quantity, UUID productId) {
        long lockId = productId.getMostSignificantBits();
        return getJdbcTemplate().update(cartQueries.getCancelReservation(),
                lockId, quantity, productId);
    }

    @Override
    public void resetCustomerCart(UUID customerId) {
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(cartQueries.getResetCart(), customerId);
    }
    @Override
    public List<CartItemDto> getCartItemsByOrderId(UUID orderId) {
        List<CartItemDto> result;

        result = getJdbcTemplate().query(cartQueries.getFindCartItemsByOrderId(),
                (rs, row) -> CartItemDto.builder()
                        .productId(UUID.fromString(rs.getString("product_id")))
                        .quantity(rs.getInt("quantity"))
                        .build(), orderId);

        return result;
    }
}
