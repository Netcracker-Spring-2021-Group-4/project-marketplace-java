package com.netcrackerg4.marketplace.repository.interfaces;

import com.netcrackerg4.marketplace.model.domain.TokenEntity;

import java.util.UUID;

public interface ITokenDao {
    void create(TokenEntity tokenEntity);

    TokenEntity read(UUID token);

    void setActivated(UUID token);
}
