package com.netcrackerg4.marketplace.repository.interfaces;

import java.util.Optional;
import java.util.UUID;

public interface ICategoryDao {
    Optional<String> findNameById(int id);
    Optional<String> getCategoryNameByProductId(UUID productId);
}
