package com.netcrackerg4.marketplace.repository.interfaces;

import com.netcrackerg4.marketplace.model.domain.DiscountEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IDiscountDao extends AbstractCrudRepository<DiscountEntity, UUID> {
    Optional<DiscountEntity> findActiveProductDiscount(UUID productId);

    List<DiscountEntity> findUnexpiredDiscounts(UUID productId);
}
