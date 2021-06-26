package com.netcrackerg4.marketplace.repository.interfaces;

import com.netcrackerg4.marketplace.model.domain.user.TokenEntity;

import java.util.Optional;
import java.util.UUID;

public interface ITokenDao {
    void create(TokenEntity tokenEntity);

    Optional<TokenEntity> read(UUID token);

    void setActivated(UUID token);
}
