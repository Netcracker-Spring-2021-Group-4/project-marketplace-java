package com.netcrackerg4.marketplace.repository.impl.order;

import com.netcrackerg4.marketplace.config.postgres_queries.order.OrderItemQueries;
import com.netcrackerg4.marketplace.model.domain.order.OrderItemEntity;
import com.netcrackerg4.marketplace.repository.interfaces.order.IOrderItemDao;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class OrderItemDaoImpl extends JdbcDaoSupport implements IOrderItemDao {
    private final OrderItemQueries orderItemQueries;

    @Autowired
    public void setParentDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @Override
    public void create(OrderItemEntity orderItem) {
        getJdbcTemplate().update(orderItemQueries.getCreateOrderItem(), orderItem.getOrderItemId(), orderItem.getQuantity(),
                orderItem.getPricePerProduct(), orderItem.getOrderId(), orderItem.getProductId());
    }

    @Override
    public void createItemsOfOrder(Collection<OrderItemEntity> orderItems) {
        for (OrderItemEntity item : orderItems) {
            getJdbcTemplate().update(orderItemQueries.getCreateOrderItem(), item.getOrderItemId(),
                    item.getQuantity(), item.getPricePerProduct(), item.getOrderId(), item.getProductId());
        }
    }

    @Override
    public Optional<OrderItemEntity> read(UUID itemId) {
        try {
            return Optional.ofNullable(getJdbcTemplate().queryForObject(orderItemQueries.getReadOrderItem(),
                    (rs, row) -> OrderItemEntity.builder()
                            .orderItemId(itemId)
                            .quantity(rs.getInt("quantity"))
                            .pricePerProduct(rs.getInt("price"))
                            .orderId(rs.getObject("order_id", UUID.class))
                            .productId(rs.getObject("product_id", UUID.class))
                            .build(), itemId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<OrderItemEntity> readItemsOfOrder(UUID orderId) {
        return getJdbcTemplate().query(orderItemQueries.getReadItemsOfOrder(), (rs, row) ->
                OrderItemEntity.builder()
                        .orderItemId(rs.getObject("order_item_id", UUID.class))
                        .quantity(rs.getInt("quantity"))
                        .pricePerProduct(rs.getInt("price"))
                        .orderId(orderId)
                        .productId(rs.getObject("product_id", UUID.class))
                        .build(), orderId);
    }
}
