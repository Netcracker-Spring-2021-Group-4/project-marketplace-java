package com.netcrackerg4.marketplace.repository.interfaces;

import com.netcrackerg4.marketplace.model.domain.CategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICategoryDao {
    Optional<String> findNameById(int id);
    List<CategoryEntity> getAll();
    Optional<String> getCategoryNameByProductId(UUID productId);
}
