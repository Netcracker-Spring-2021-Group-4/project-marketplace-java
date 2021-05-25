package com.netcrackerg4.marketplace.repository.interfaces;


import com.netcrackerg4.marketplace.model.domain.AppProductEntity;

public interface IProductDao  {
    void createProduct(AppProductEntity item);

    Integer findCategoryIdByCategoryName(String name);
}
