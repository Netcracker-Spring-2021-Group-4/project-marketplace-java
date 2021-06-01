package com.netcrackerg4.marketplace.repository.impl;

import com.netcrackerg4.marketplace.config.postgres_queries.CategoryQueries;
import com.netcrackerg4.marketplace.repository.interfaces.ICategoryDao;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class CategoryDaoImpl extends JdbcDaoSupport implements ICategoryDao {
    private final CategoryQueries categoryQueries;

    @Autowired
    public void setParentDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @Override
    public Optional<String> findNameById(int id) {
        assert getJdbcTemplate() != null;
        Optional<String> categoryName;
        try {
            categoryName = Optional.ofNullable(
                    getJdbcTemplate().queryForObject(categoryQueries.getFindById(),
                            (rs, row) -> rs.getString("product_category_name"), id)
            );
        } catch (EmptyResultDataAccessException e ) {
            categoryName = Optional.empty();
        }
        return categoryName;
    }
}
