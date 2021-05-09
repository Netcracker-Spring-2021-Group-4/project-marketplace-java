package com.netcrackerg4.marketplace.repository.impl;

import com.netcrackerg4.marketplace.config.postgres_queries.UserRoleQueries;
import com.netcrackerg4.marketplace.repository.interfaces.IAuthDao;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class AuthDaoImpl extends JdbcDaoSupport implements IAuthDao {
    private final UserRoleQueries roleQueries;

    @Autowired
    public void setParentDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @Override
    public Optional<Integer> getRoleIdByName(String roleName) {
        assert getJdbcTemplate() != null;
        return Optional.ofNullable(getJdbcTemplate().queryForObject(roleQueries.getFindRoleIdByRoleName(),
                Integer.class, roleName));
    }

    @Override
    public List<GrantedAuthority> getPermissionsByRoleId(int roleId) {
        return null;
    }
}
