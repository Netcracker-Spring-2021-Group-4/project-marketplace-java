package com.netcrackerg4.marketplace.repository.impl.order;

import com.netcrackerg4.marketplace.config.postgres_queries.order.AddressQueries;
import com.netcrackerg4.marketplace.model.domain.AddressEntity;
import com.netcrackerg4.marketplace.repository.interfaces.order.IAddressDao;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class AddressDaoImpl extends JdbcDaoSupport implements IAddressDao {
    private final AddressQueries addressQueries;

    @Autowired
    public void setParentDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @Override
    public void create(AddressEntity addressEntity) {
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(addressQueries.getCreateAddress(), addressEntity.getAddressId(), addressEntity.getCity(),
                addressEntity.getStreet(), addressEntity.getBuilding(), addressEntity.getFlat(), addressEntity.getCustomerId());
    }

    @Override
    public Optional<AddressEntity> read(UUID addressId) {
        assert getJdbcTemplate() != null;
        try {
            return Optional.ofNullable(getJdbcTemplate().queryForObject(addressQueries.getReadAddress(), (rs, row) -> AddressEntity.builder()
                    .addressId(addressId)
                    .city(rs.getString("city"))
                    .street(rs.getString("street"))
                    .building(rs.getString("building"))
                    .flat(rs.getObject("flat", Integer.class))
                    .customerId(rs.getObject("customer_id", UUID.class)).build(), addressId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
