package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.response.FilterInfo;

import java.util.List;

public interface ICategoryService {
    String findNameById(int id);
    List<FilterInfo.CategoryResponse> categoriesWithAmountOfProduct();

    List<Integer> getCategoriesIds();
}
