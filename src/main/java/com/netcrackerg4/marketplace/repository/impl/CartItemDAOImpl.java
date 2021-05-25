package com.netcrackerg4.marketplace.repository.impl;

import com.netcrackerg4.marketplace.config.postgres_queries.CartQueries;
import com.netcrackerg4.marketplace.model.domain.CartItemEntity;
import com.netcrackerg4.marketplace.model.dto.product.CartItemDto;
import com.netcrackerg4.marketplace.repository.interfaces.ICartItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class CartItemDAOImpl extends JdbcDaoSupport implements ICartItemDao {

    private final CartQueries cartQueries;

    @Autowired
    public CartItemDAOImpl(DataSource ds, CartQueries cartQueries) {
        this.cartQueries = cartQueries;
        super.setDataSource(ds);
            }

    @Override
    public void addToCart(CartItemEntity item) {
        assert getJdbcTemplate() != null;
        getJdbcTemplate()
                .update(cartQueries.getAddToCart(),
                        item.getCartItemId(),
                        item.getQuantity(),
                        item.getTimestampAdded(),
                        item.getCustomerId(),
                        item.getProductId());

    }
}
