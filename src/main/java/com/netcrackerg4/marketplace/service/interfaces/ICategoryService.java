package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.domain.CategoryEntity;

import java.util.List;

import com.netcrackerg4.marketplace.model.response.FilterInfo;

import java.util.List;

import com.netcrackerg4.marketplace.model.domain.CategoryEntity;
import com.netcrackerg4.marketplace.model.response.FilterInfo;

import java.util.List;
import java.util.UUID;

public interface ICategoryService {
    String findNameById(int id);
    List<FilterInfo.CategoryResponse> categoriesWithAmountOfProduct();
    List<Integer> getCategoriesIds();
    List<CategoryEntity> getAll();
    String getCategoryNameByProductId(UUID productId);
}
