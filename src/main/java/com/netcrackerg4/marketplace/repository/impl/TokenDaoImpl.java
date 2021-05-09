package com.netcrackerg4.marketplace.repository.impl;

import com.netcrackerg4.marketplace.config.postgres_queries.TokenQueries;
import com.netcrackerg4.marketplace.model.domain.TokenEntity;
import com.netcrackerg4.marketplace.repository.interfaces.ITokenDao;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;

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
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(tokenQueries.getCreateToken(), tokenEntity.getTokenValue(),
                tokenEntity.getUserEmail(), new Timestamp(tokenEntity.getExpiresAt().toEpochMilli()));
    }

    @Override
    public TokenEntity read(String tokenValue) {
        // TODO: handle empty EmptyResultDataAccessException
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().queryForObject(tokenQueries.getReadToken(), (rs, row) ->
                        new TokenEntity(rs.getString("token"), rs.getString("user_email"),
                                rs.getTimestamp("expired_at").toInstant(), rs.getBoolean("is_activated")),
                tokenValue);
    }

    @Override
    public void setActivated(String tokenValue) {
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(tokenQueries.getActivateToken(), tokenValue);
    }
}
