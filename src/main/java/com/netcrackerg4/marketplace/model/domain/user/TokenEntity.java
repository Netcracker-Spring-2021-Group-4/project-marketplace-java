package com.netcrackerg4.marketplace.model.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenEntity {
    private UUID tokenValue;
    private String userEmail;
    private Instant expiresAt;
    private boolean isActivated;
    private boolean isForPassword;
}
