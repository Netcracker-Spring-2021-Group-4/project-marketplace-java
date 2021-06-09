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
    private final int ORDER_SESSION_ID = 1;

    @Autowired
    public void setParentDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

//    @Override
//    public void requestOrderLock() {
//        getJdbcTemplate().queryForObject(lockQueries.getCaptureSessional(), Object.class, ORDER_SESSION_ID);
//    }

    @Override
    public void requestTransactionLock(long lockId) {
        getJdbcTemplate().queryForObject(lockQueries.getCaptureTransactional(), Object.class, lockId);
    }

//    @Override
//    public void releaseOrderLock() {
//        getJdbcTemplate().queryForObject(lockQueries.getReleaseSessional(), Object.class, ORDER_SESSION_ID);
//    }
}
