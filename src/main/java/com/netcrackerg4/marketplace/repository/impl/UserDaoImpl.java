package com.netcrackerg4.marketplace.repository.impl;

import com.netcrackerg4.marketplace.config.PostgresUserQueries;
import com.netcrackerg4.marketplace.model.domain.AppUser;
import com.netcrackerg4.marketplace.repository.interfaces.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository
public class UserDaoImpl extends JdbcDaoSupport implements UserDAO {

    private PostgresUserQueries postgresUserQueries;
    private DataSource dataSource;

    @Autowired
    public UserDaoImpl(PostgresUserQueries postgresUserQueries, DataSource dataSource) {
        this.postgresUserQueries = postgresUserQueries;
        setDataSource(dataSource);
    }

    @Override
    public Optional<AppUser> findByIdx(String idx) {
        assert getJdbcTemplate() != null;
        Optional<AppUser> user;
        try{
            user = Optional.ofNullable(getJdbcTemplate().queryForObject(postgresUserQueries.getGetByEmail(),(rs, row) ->
                    new AppUser(rs.getString("email"),
                            rs.getString("password"),
                            rs.getInt("role_id")
                    )
                    , idx)
            );
        } catch (EmptyResultDataAccessException e) {
            user = Optional.empty();
        }
        return user;
    }

    @Override
    public Set<GrantedAuthority> getAuthorities(int roleId) {
        assert getJdbcTemplate() != null;
        var list = getJdbcTemplate().query(postgresUserQueries.getGetAuthorities(), (rs, row) ->
                new SimpleGrantedAuthority(rs.getString("authority")),
                roleId, roleId);
        return new HashSet<>(list);
    }

    // TODO
    @Override
    public void create(AppUser item) {

    }
}
