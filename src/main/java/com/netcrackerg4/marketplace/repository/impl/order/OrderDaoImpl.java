package com.netcrackerg4.marketplace.repository.impl.order;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.netcrackerg4.marketplace.config.postgres_queries.order.OrderQueries;
import com.netcrackerg4.marketplace.model.domain.order.OrderEntity;
import com.netcrackerg4.marketplace.model.enums.OrderStatus;
import com.netcrackerg4.marketplace.repository.interfaces.ICartItemDao;
import com.netcrackerg4.marketplace.repository.interfaces.IUserDao;
import com.netcrackerg4.marketplace.repository.interfaces.order.IAddressDao;
import com.netcrackerg4.marketplace.repository.interfaces.order.IOrderDao;
import com.netcrackerg4.marketplace.repository.interfaces.order.IOrderItemDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderDaoImpl extends JdbcDaoSupport implements IOrderDao {
    private final OrderQueries orderQueries;
    private BiMap<OrderStatus, Integer> orderStatusIds;
    private final ICartItemDao cartItemDao;
    private final IAddressDao addressDao;
    private final IUserDao userDao;
    private final IOrderItemDao orderItemDao;

    @Autowired
    public void setParentDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @PostConstruct
    private void initOrderStates() {
        orderStatusIds = HashBiMap.create();
        getJdbcTemplate().query(orderQueries.getReadStatusIds(), (rs, row) ->
                orderStatusIds.put(OrderStatus.valueOf(rs.getString("status_name")),
                        rs.getInt("status_id")));
    }

    @Override
    public void create(OrderEntity item) {
        getJdbcTemplate().update(orderQueries.getCreateOrder(), item.getOrderId(), item.getPlacedAt(),
                item.getPhoneNumber(), item.getComment(), orderStatusIds.get(item.getStatus()),
                item.getAddress().getAddressId(),
                item.getCustomer() != null ? item.getCustomer().getUserId() : null);
        if (item.getCustomer() != null)
            cartItemDao.resetCustomerCart(item.getCustomer().getUserId());
        orderItemDao.createItemsOfOrder(item.getOrderItems());
    }

    @Override
    public Optional<OrderEntity> read(UUID key) {
        try {
            OrderEntity coreOrder = getJdbcTemplate().queryForObject(orderQueries.getReadOrder(), (rs, row) -> OrderEntity.builder()
                    .orderId(key)
                    .placedAt(rs.getTimestamp("placed_at"))
                    .phoneNumber(rs.getString("phone_number"))
                    .comment(rs.getString("comment"))
                    .status(orderStatusIds.inverse().get(rs.getInt("status_id")))
                    .address(addressDao.read(rs.getObject("address_id", UUID.class)).orElseThrow())
                    .customer(userDao.read(rs.getObject("customer_id", UUID.class)).orElseThrow())
                    .build(), key);
            if (coreOrder == null) return Optional.empty();

            coreOrder.setOrderItems(orderItemDao.readItemsOfOrder(coreOrder.getOrderId()));
            return Optional.of(coreOrder);
        } catch (EmptyResultDataAccessException | NoSuchElementException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(OrderEntity updItem) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(UUID key) {
        throw new UnsupportedOperationException();
    }
}
