package com.netcrackerg4.marketplace.repository.impl;

import com.netcrackerg4.marketplace.config.postgres_queries.UserQueries;
import com.netcrackerg4.marketplace.exception.BadCodeError;
import com.netcrackerg4.marketplace.model.domain.AppProductEntity;
import com.netcrackerg4.marketplace.repository.interfaces.IProductDao;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class ProductDaoImpl extends JdbcDaoSupport implements IProductDao {
    private final UserQueries userQueries;

    @Override
    public void createProduct(AppProductEntity item) {
        assert getJdbcTemplate() != null;
        int categoryId = findCategoryIdByCategoryName(item.getCategory().name());
        getJdbcTemplate().update(userQueries.getCreateProduct(), item.getName(), item.getImageUrl(),item.getDescription(),
                item.getPrice(),item.getInStock(),item.getReserved(),item.getAvailabilityDate(),item.getIsActive(),
                categoryId);
    }

    @Override
    public Integer findCategoryIdByCategoryName(String name) {
        assert getJdbcTemplate() != null;
        Integer categoryId = getJdbcTemplate().queryForObject(userQueries.getFindCategoryIdByName(), Integer.class, name);
        if (categoryId == null) throw new BadCodeError();
        return categoryId;
    }
}
