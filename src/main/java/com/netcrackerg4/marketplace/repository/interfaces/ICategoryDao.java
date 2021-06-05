package com.netcrackerg4.marketplace.repository.interfaces;

import com.netcrackerg4.marketplace.model.response.FilterInfo;
import com.netcrackerg4.marketplace.model.domain.CategoryEntity;
import java.util.List;
import java.util.Optional;

public interface ICategoryDao {
    Optional<String> findNameById(int id);

    List<FilterInfo.CategoryResponse> findCategoriesWithAmountOfProducts();

    List<Integer> findCategoriesIds();
    List<CategoryEntity> getAll();
}
