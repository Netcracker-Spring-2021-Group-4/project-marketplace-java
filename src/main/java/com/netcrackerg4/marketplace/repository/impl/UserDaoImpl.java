package com.netcrackerg4.marketplace.repository.impl;

import com.netcrackerg4.marketplace.config.postgres_queries.ProductQueries;
import com.netcrackerg4.marketplace.config.postgres_queries.UserQueries;
import com.netcrackerg4.marketplace.exception.BadCodeError;
import com.netcrackerg4.marketplace.model.domain.AppProductEntity;
import com.netcrackerg4.marketplace.model.domain.AppUserEntity;
import com.netcrackerg4.marketplace.model.enums.UserRole;
import com.netcrackerg4.marketplace.model.enums.UserStatus;
import com.netcrackerg4.marketplace.repository.interfaces.IProductDao;
import com.netcrackerg4.marketplace.repository.interfaces.IUserDao;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class UserDaoImpl extends JdbcDaoSupport implements IUserDao {

    private final UserQueries userQueries;
    private final ProductQueries productQueries;

    @Autowired
    public void setParentDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @Override
    public Optional<AppUserEntity> findByEmail(String idx) {
        assert getJdbcTemplate() != null;
        Optional<AppUserEntity> user;
        try {
            user = Optional.ofNullable(getJdbcTemplate().queryForObject(userQueries.getGetByEmail(), (rs, row) ->
                            AppUserEntity.builder()
                                    .userId(rs.getString("user_id"))
                                    .email(rs.getString("email"))
                                    .password(rs.getString("password"))
                                    .firstName(rs.getString("first_name"))
                                    .lastName(rs.getString("last_name"))
                                    .phoneNumber(rs.getString("phone_number"))
                                    .status(UserStatus.valueOf(rs.getString("status_name")))
                                    .role(UserRole.valueOf(rs.getString("role_name"))).build()
                    , idx)
            );
        } catch (EmptyResultDataAccessException e) {
            user = Optional.empty();
        }
        return user;
    }

    @Override
    public void create(AppUserEntity item) {
        assert getJdbcTemplate() != null;
        int statusId = findStatusIdByStatusName(item.getStatus().name());
        int roleId = findRoleIdByRoleName(item.getRole().name());
        getJdbcTemplate().update(userQueries.getCreateNew(), item.getEmail(), item.getPassword(), item.getFirstName(),
                item.getLastName(), item.getPhoneNumber(), roleId, statusId);
    }

    @Override
    public AppUserEntity read(String key) {
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().queryForObject(userQueries.getFindUserById(), (rs, row) ->
                AppUserEntity.builder()
                        .userId(key)
                        .email(rs.getString("email"))
                        .password(rs.getString("password"))
                        .firstName(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .phoneNumber(rs.getString("phone_number"))
                        .status(UserStatus.valueOf(rs.getString("status_name")))
                        .role(UserRole.valueOf(rs.getString("role_name")))
                        .build(), UUID.fromString(key)
        );
    }

    @Override
    public void update(AppUserEntity item) {
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(userQueries.getUpdateUserInfo(), item.getFirstName(), item.getLastName(), item.getPhoneNumber(),
                item.getEmail());
    }

    @Override
    public void delete(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<GrantedAuthority> getAuthorities(int roleId) {
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().query(userQueries.getGetAuthorities(), (rs, row) ->
                        new SimpleGrantedAuthority(rs.getString("authority")),
                roleId, roleId);
    }


    @Override
    public void updateStatus(String email, UserStatus status) {
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(userQueries.getUpdateStatus(), status.name(), email);
    }

    @Override
    public void updatePassword(String email, String password) {
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(userQueries.getUpdatePassword(), password, email);
    }

    @Override
    public Integer findStatusIdByStatusName(String name) {
        assert getJdbcTemplate() != null;
        Integer statusId = getJdbcTemplate().queryForObject(userQueries.getFindStatusIdByName(), Integer.class, name);
        if (statusId == null) throw new BadCodeError();
        return statusId;
    }

    @Override
    public Integer findRoleIdByRoleName(String roleName) {
        assert getJdbcTemplate() != null;
        Integer roleId = getJdbcTemplate().queryForObject(userQueries.getFindRoleIdByName(), Integer.class, roleName);
        if (roleId == null) throw new BadCodeError();
        return roleId;
    }
}
