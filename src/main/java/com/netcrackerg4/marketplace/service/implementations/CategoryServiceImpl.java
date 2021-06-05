package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.model.domain.CategoryEntity;
import com.netcrackerg4.marketplace.repository.interfaces.ICategoryDao;
import com.netcrackerg4.marketplace.repository.interfaces.IS3Dao;
import com.netcrackerg4.marketplace.service.interfaces.ICategoryService;
import com.netcrackerg4.marketplace.service.interfaces.IS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public List<CategoryEntity> getAll() {
        return categoryDao.getAll();
    }

    @Override
    public String getCategoryNameByProductId(UUID productId) {
        return (String) categoryDao.getCategoryNameByProductId(productId)
            .orElseThrow(()-> new IllegalStateException(String.format("Category name for this product not found")));
    }
}
