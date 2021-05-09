package com.netcrackerg4.marketplace.repository.interfaces;

import com.netcrackerg4.marketplace.model.domain.TokenEntity;

public interface ITokenDao {
    void create(TokenEntity tokenEntity);

    TokenEntity read(String tokenValue);
}
