package com.netcrackerg4.marketplace.repository.impl;

import com.netcrackerg4.marketplace.config.postgres_queries.TokenQueries;
import com.netcrackerg4.marketplace.model.domain.user.TokenEntity;
import com.netcrackerg4.marketplace.repository.interfaces.ITokenDao;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class TokenDaoImpl extends JdbcDaoSupport implements ITokenDao {
    private final TokenQueries tokenQueries;

    @Autowired
    public void setParentDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @Override
    public void create(TokenEntity tokenEntity) {
        getJdbcTemplate().update(tokenQueries.getCreateToken(), tokenEntity.getTokenValue(),
                tokenEntity.getUserEmail(), new Timestamp(tokenEntity.getExpiresAt().toEpochMilli()));
    }

    @Override
    public TokenEntity read(UUID token) {
        // TODO: handle empty EmptyResultDataAccessException
        return getJdbcTemplate().queryForObject(tokenQueries.getReadToken(), (rs, row) ->
                        new TokenEntity(rs.getObject("token", UUID.class), rs.getString("user_email"),
                                rs.getTimestamp("expired_at").toInstant(), rs.getBoolean("is_activated")),
                token);
    }

    @Override
    public void setActivated(UUID token) {
        getJdbcTemplate().update(tokenQueries.getActivateToken(), token);
    }
}
