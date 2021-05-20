package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.domain.TokenEntity;

import java.util.UUID;

public interface IMailService {
    void sendConfirmSignupLetter(String email, String firstName, String lastName, UUID token);

    void sendPasswordResetEmail(String email, TokenEntity resetToken);
}
