package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.domain.CategoryEntity;

import java.util.List;

public interface ICategoryService {
    String findNameById(int id);
    List<CategoryEntity> getAll();
}
