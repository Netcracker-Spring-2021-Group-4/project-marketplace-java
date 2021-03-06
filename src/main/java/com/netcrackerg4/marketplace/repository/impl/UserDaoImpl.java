package com.netcrackerg4.marketplace.repository.impl;

import com.netcrackerg4.marketplace.config.postgres_queries.UserQueries;
import com.netcrackerg4.marketplace.exception.BadCodeError;
import com.netcrackerg4.marketplace.model.domain.user.AppUserEntity;
import com.netcrackerg4.marketplace.model.dto.user.UserAdminView;
import com.netcrackerg4.marketplace.model.enums.UserRole;
import com.netcrackerg4.marketplace.model.enums.UserStatus;
import com.netcrackerg4.marketplace.repository.interfaces.IUserDao;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class UserDaoImpl extends JdbcDaoSupport implements IUserDao {

    private final UserQueries userQueries;

    @Autowired
    public void setParentDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @Override
    public Optional<AppUserEntity> findByEmail(String email) {
        Optional<AppUserEntity> user;
        try {
            user = Optional.ofNullable(getJdbcTemplate().queryForObject(userQueries.getGetByEmail(), (rs, row) ->
                            AppUserEntity.builder()
                                    .userId( UUID.fromString(rs.getString("user_id")))
                                    .email(rs.getString("email"))
                                    .password(rs.getString("password"))
                                    .firstName(rs.getString("first_name"))
                                    .lastName(rs.getString("last_name"))
                                    .phoneNumber(rs.getString("phone_number"))
                                    .status(UserStatus.valueOf(rs.getString("status_name")))
                                    .role(UserRole.valueOf(rs.getString("role_name"))).build()
                    , email)
            );
        } catch (EmptyResultDataAccessException e) {
            user = Optional.empty();
        }
        return user;
    }

    @Override
    public void create(AppUserEntity item) {
        int statusId = findStatusIdByStatusName(item.getStatus().name());
        int roleId = findRoleIdByRoleName(item.getRole().name());
        getJdbcTemplate().update(userQueries.getCreateNew(), item.getEmail(), item.getPassword(), item.getFirstName(),
                item.getLastName(), item.getPhoneNumber(), roleId, statusId);
    }

    @Override
    public Optional<AppUserEntity> read(UUID key) {
        Optional<AppUserEntity> result;
        try {
            result = Optional.ofNullable(getJdbcTemplate().queryForObject(userQueries.getFindUserById(), (rs, row) ->
                    AppUserEntity.builder()
                            .userId(key)
                            .email(rs.getString("email"))
                            .password(rs.getString("password"))
                            .firstName(rs.getString("first_name"))
                            .lastName(rs.getString("last_name"))
                            .phoneNumber(rs.getString("phone_number"))
                            .status(UserStatus.valueOf(rs.getString("status_name")))
                            .role(UserRole.valueOf(rs.getString("role_name")))
                            .build(), key
            ));
        } catch(EmptyResultDataAccessException e) {
            result = Optional.empty();
        }

        return result;
    }

    @Override
    public void update(AppUserEntity item) {
        getJdbcTemplate().update(userQueries.getUpdateUserInfo(), item.getFirstName(), item.getLastName(), item.getPhoneNumber(),
                item.getEmail());
    }

    @Override
    public void delete(UUID key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<GrantedAuthority> getAuthorities(int roleId) {
        return getJdbcTemplate().query(userQueries.getGetAuthorities(), (rs, row) ->
                        new SimpleGrantedAuthority(rs.getString("authority")),
                roleId, roleId);
    }

    @Override
    public void updateStatus(String email, UserStatus status) {
        getJdbcTemplate().update(userQueries.getUpdateStatus(), status.name(), email);
    }

    @Override
    public void updatePassword(String email, String password) {
        getJdbcTemplate().update(userQueries.getUpdatePassword(), password, email);
    }

    @Override
    public Integer findStatusIdByStatusName(String name) {
        Integer statusId = getJdbcTemplate().queryForObject(userQueries.getFindStatusIdByName(), Integer.class, name);
        if (statusId == null) throw new BadCodeError();
        return statusId;
    }

    @Override
    public Integer findRoleIdByRoleName(String roleName) {
        Integer roleId = getJdbcTemplate().queryForObject(userQueries.getFindRoleIdByName(), Integer.class, roleName);
        if (roleId == null) throw new BadCodeError();
        return roleId;
    }

    @Override
    public List<UserAdminView> findUsersByFilter(List<UserRole> targetRoles, List<UserStatus> targetStatuses,
                                                 String firstName, String lastName, int pageSize, int pageNo) {

        MapSqlParameterSource namedParams = new MapSqlParameterSource() {{
            addValue("roles", targetRoles.stream().map(UserRole::toString).collect(Collectors.toList()));
            addValue("statuses", targetStatuses.stream().map(UserStatus::toString).collect(Collectors.toList()));
            addValue("fst_seq", "%" + firstName + "%");
            addValue("last_seq", "%" + lastName + "%");
            addValue("limit", pageSize);
            addValue("offset", pageSize * pageNo);
        }};

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
        return namedParameterJdbcTemplate.query(userQueries.getFindByRoleStatusNames(),
                namedParams,
                (rs, row) ->
                        UserAdminView.builder()
                                .userId(rs.getObject("user_id", UUID.class).toString())
                                .email(rs.getString("email"))
                                .firstName(rs.getString("first_name"))
                                .lastName(rs.getString("last_name"))
                                .phoneNumber(rs.getString("phone_number"))
                                .status(UserStatus.valueOf(rs.getString("status_name")))
                                .role(UserRole.valueOf(rs.getString("role_name")))
                                .build()
        );
    }

    @Override
    public int countFilteredUsers(List<UserRole> targetRoles, List<UserStatus> targetStatuses, String firstName, String lastName) {

        MapSqlParameterSource namedParams = new MapSqlParameterSource() {{
            addValue("roles", targetRoles.stream().map(UserRole::toString).collect(Collectors.toList()));
            addValue("statuses", targetStatuses.stream().map(UserStatus::toString).collect(Collectors.toList()));
            addValue("fst_seq", "%" + firstName + "%");
            addValue("last_seq", "%" + lastName + "%");
        }};

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
        Integer numFound = namedParameterJdbcTemplate.queryForObject(
                userQueries.getCountByRoleStatusNames(), namedParams, Integer.class
        );
        return numFound != null ? numFound : 0;
    }
}