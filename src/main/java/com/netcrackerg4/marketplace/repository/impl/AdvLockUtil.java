package com.netcrackerg4.marketplace.repository.impl;

import com.netcrackerg4.marketplace.config.postgres_queries.AdvLockQueries;
import com.netcrackerg4.marketplace.repository.interfaces.IAdvLockUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@AllArgsConstructor
@Repository
public class AdvLockUtil extends JdbcDaoSupport implements IAdvLockUtil {
    private final AdvLockQueries lockQueries;

    @Autowired
    public void setParentDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @Override
    public void requestTransactionLock(long lockId) {
        getJdbcTemplate().queryForObject(lockQueries.getTransaction(), Object.class, lockId);
    }
}
