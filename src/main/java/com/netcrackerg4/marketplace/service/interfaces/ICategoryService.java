package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.domain.CategoryEntity;

import java.util.List;
import java.util.UUID;

public interface ICategoryService {
    String findNameById(int id);
    List<CategoryEntity> getAll();
    String getCategoryNameByProductId(UUID productId);
}
