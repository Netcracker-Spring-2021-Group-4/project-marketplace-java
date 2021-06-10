package com.netcrackerg4.marketplace.repository.interfaces.order;

import com.netcrackerg4.marketplace.model.domain.AddressEntity;

import java.util.Optional;
import java.util.UUID;

public interface IAddressDao {
    void create(AddressEntity addressEntity);

    Optional<AddressEntity> read(UUID addressId);
}
