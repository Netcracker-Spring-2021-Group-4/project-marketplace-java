package com.netcrackerg4.marketplace.repository.impl;

import com.netcrackerg4.marketplace.config.postgres_queries.ProductQueries;
import com.netcrackerg4.marketplace.exception.BadCodeError;
import com.netcrackerg4.marketplace.model.domain.AppProductEntity;
import com.netcrackerg4.marketplace.repository.interfaces.IProductDao;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
@AllArgsConstructor
public class ProductDaoImpl extends JdbcDaoSupport implements IProductDao {
    private final ProductQueries productQueries;

    @Autowired
    public void setParentDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }
    @Override
    public Integer findCategoryIdByCategoryName(String name) {
        assert getJdbcTemplate() != null;
        Integer categoryId = getJdbcTemplate().queryForObject(productQueries.getFindCategoryIdByName(), Integer.class, name);
        if (categoryId == null) throw new BadCodeError();
        return categoryId;
    }

    @Override
    public void createProduct(AppProductEntity item) {
        assert getJdbcTemplate() != null;
        int categoryId = findCategoryIdByCategoryName(item.getCategory().name());
        getJdbcTemplate().update(productQueries.getCreateProduct(), item.getProductId(), item.getName(),
                item.getImageUrl(),item.getDescription(), item.getPrice(),item.getInStock(),item.getReserved(),
                item.getAvailabilityDate(),item.getIsActive(),
                categoryId);
    }
}
