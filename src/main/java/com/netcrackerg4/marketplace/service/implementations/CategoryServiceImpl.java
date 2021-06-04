package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.model.response.FilterInfo;
import com.netcrackerg4.marketplace.model.domain.CategoryEntity;
import com.netcrackerg4.marketplace.repository.interfaces.ICategoryDao;
import com.netcrackerg4.marketplace.service.interfaces.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {
    private final ICategoryDao categoryDao;


    @Override
    public String findNameById(int id) {
        return categoryDao.findNameById(id)
                .orElseThrow(() -> new IllegalStateException(String.format("Category with id %d not found", id)));
    }

    @Override
    public List<FilterInfo.CategoryResponse> categoriesWithAmountOfProduct() {
        return categoryDao.findCategoriesWithAmountOfProducts();
    }

    @Override
    public List<Integer> getCategoriesIds() {
        return categoryDao.findCategoriesIds();
    }

    @Override
    public List<CategoryEntity> getAll() {
        return categoryDao.getAll();
    }
}
