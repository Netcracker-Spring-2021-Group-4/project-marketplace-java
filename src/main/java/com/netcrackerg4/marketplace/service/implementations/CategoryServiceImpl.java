package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.repository.interfaces.ICategoryDao;
import com.netcrackerg4.marketplace.service.interfaces.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public String getCategoryNameByProductId(UUID productId) {
      return categoryDao.getCategoryNameByProductId(productId)
            .orElseThrow(()-> new IllegalStateException(String.format("Category name for this product not found")));
    }
}
