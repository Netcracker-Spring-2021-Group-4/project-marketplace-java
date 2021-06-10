package com.netcrackerg4.marketplace.repository.impl.order;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.netcrackerg4.marketplace.config.postgres_queries.order.OrderQueries;
import com.netcrackerg4.marketplace.exception.BadCodeError;
import com.netcrackerg4.marketplace.model.domain.AddressEntity;
import com.netcrackerg4.marketplace.model.domain.order.OrderEntity;
import com.netcrackerg4.marketplace.model.domain.user.AppUserEntity;
import com.netcrackerg4.marketplace.model.enums.OrderStatus;
import com.netcrackerg4.marketplace.repository.interfaces.IUserDao;
import com.netcrackerg4.marketplace.repository.interfaces.order.IAddressDao;
import com.netcrackerg4.marketplace.repository.interfaces.order.IOrderDao;
import com.netcrackerg4.marketplace.repository.interfaces.order.IOrderItemDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderDaoImpl extends JdbcDaoSupport implements IOrderDao {
    private final OrderQueries orderQueries;
    private BiMap<OrderStatus, Integer> orderStatusIds;
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
                item.getPhoneNumber(), item.getComment(), item.getFirstName(), item.getLastName(),
                orderStatusIds.get(item.getStatus()), item.getAddress().getAddressId(),
                item.getCustomer() != null ? item.getCustomer().getUserId() : null);
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
                    .firstName(rs.getString("first_name"))
                    .lastName(rs.getString("last_name"))
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

    @Override
    public List<OrderEntity> readCourierOrders(UUID courierId, List<OrderStatus> orderStatuses, int pageSize, int pageNo) {
        List<Integer> statusIds = orderStatuses.stream().map(orderStatusIds::get).collect(Collectors.toList());
        MapSqlParameterSource namedParams = new MapSqlParameterSource() {{
            addValue("courier_id", courierId);
            addValue("prod_status_ids", statusIds);
            addValue("limit", pageSize);
            addValue("offset", pageSize * pageNo);
        }};
        return doReadOrders(orderQueries.getFindCourierOrders(), namedParams, true);
    }

    private List<OrderEntity> doReadOrders(String query, MapSqlParameterSource params, boolean canHaveNames) {
        NamedParameterJdbcTemplate namedJdbc = new NamedParameterJdbcTemplate(getJdbcTemplate());
        List<String> errors = new ArrayList<>(0);
        try {
            List<OrderEntity> orders = namedJdbc.query(query, params, (rs, row) -> {
                var orderBuilder = OrderEntity.builder()
                        .orderId(rs.getObject("order_id", UUID.class))
                        .placedAt(rs.getTimestamp("placed_at"))
                        .phoneNumber(rs.getString("phone_number"))
                        .comment(rs.getString("comment"))
                        .status(orderStatusIds.inverse().get(rs.getInt("status_id")));

                if (canHaveNames) {
                    orderBuilder.firstName(rs.getString("first_name"));
                    orderBuilder.lastName(rs.getString("last_name"));
                }

                UUID addressId = rs.getObject("address_id", UUID.class);
                Optional<AddressEntity> maybeAddress = addressDao.read(addressId);
                if (maybeAddress.isPresent())
                    orderBuilder.address(maybeAddress.get());
                else {
                    errors.add(String.format("address with id '%s' is missing", addressId.toString()));
                    return null;
                }
                UUID customerId = rs.getObject("customer_id", UUID.class);
                Optional<AppUserEntity> maybeCustomer = userDao.read(customerId);
                maybeCustomer.ifPresent(orderBuilder::customer);

                OrderEntity order = orderBuilder.build();
                order.setOrderItems(orderItemDao.readItemsOfOrder(order.getOrderId()));

                return order;
            });

            if (errors.size() != 0) {
                throw new IllegalStateException("Encountered errors:\n" + String.join(",", errors));
            }

            return orders;
        } catch (DataAccessException e) {
            throw new IllegalStateException("Failed to find orders.", e);
        }
    }

    @Override
    public int countCourierOrders(UUID courierId, List<OrderStatus> orderStatuses) {
        try {
            List<Integer> statusIds = orderStatuses.stream().map(orderStatusIds::get).collect(Collectors.toList());

            MapSqlParameterSource namedParams = new MapSqlParameterSource() {{
                addValue("courier_id", courierId);
                addValue("prod_status_ids", statusIds);
            }};
            Integer maybeOrdersNum = new NamedParameterJdbcTemplate(getJdbcTemplate())
                    .queryForObject(orderQueries.getCountCourierOrdersNum(), namedParams, Integer.class);
            if (maybeOrdersNum != null) return maybeOrdersNum;
            else throw new BadCodeError("'null' orders assigned to a courier");
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalStateException("error counting number of courier's orders", e);
        }
    }

    @Override
    public List<OrderEntity> readCustomerOrders(UUID customerId, List<OrderStatus> orderStatuses, int pageSize, int pageNo) {
        List<Integer> statusIds = orderStatuses.stream().map(orderStatusIds::get).collect(Collectors.toList());
        MapSqlParameterSource namedParams = new MapSqlParameterSource() {{
            addValue("customer_id", customerId);
            addValue("prod_status_ids", statusIds);
            addValue("limit", pageSize);
            addValue("offset", pageSize * pageNo);
        }};
        return doReadOrders(orderQueries.getFindCourierOrders(), namedParams, false);
    }

    @Override
    public int countCustomerOrders(UUID customerId, List<OrderStatus> orderStatuses) {
        try {
            List<Integer> statusIds = orderStatuses.stream().map(orderStatusIds::get).collect(Collectors.toList());

            MapSqlParameterSource namedParams = new MapSqlParameterSource() {{
                addValue("customer_id", customerId);
                addValue("prod_status_ids", statusIds);
            }};
            Integer maybeOrdersNum = new NamedParameterJdbcTemplate(getJdbcTemplate())
                    .queryForObject(orderQueries.getCountCustomerOrdersNum(), namedParams, Integer.class);
            if (maybeOrdersNum != null) return maybeOrdersNum;
            else throw new BadCodeError("'null' orders assigned to a customer");
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalStateException("error counting number of customer's orders", e);
        }
    }

    @Override
    public Collection<UUID> updateStatusIfStarted(OrderStatus current, OrderStatus next) {
        int currStatusId = orderStatusIds.get(current);
        int updStatusId = orderStatusIds.get(next);
        return getJdbcTemplate().queryForList(orderQueries.getUpdateStatusWithinSlot(), UUID.class, updStatusId, currStatusId);
    }

    @Override
    public Collection<UUID> updateStatusIfFinished(OrderStatus current, OrderStatus next) {
        int currStatusId = orderStatusIds.get(current);
        int updStatusId = orderStatusIds.get(next);
        try {
            return getJdbcTemplate().queryForList(orderQueries.getUpdateStatusAfterSlot(), UUID.class, updStatusId, currStatusId);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


}
