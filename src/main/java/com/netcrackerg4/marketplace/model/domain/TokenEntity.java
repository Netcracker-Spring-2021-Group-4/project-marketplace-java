package com.netcrackerg4.marketplace.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenEntity {
    private String tokenValue;
    private String userEmail;
    //    private long issuedAt;
    private Instant expiresAt;
    private boolean isActivated;
}
