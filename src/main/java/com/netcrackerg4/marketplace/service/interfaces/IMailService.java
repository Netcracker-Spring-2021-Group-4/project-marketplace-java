package com.netcrackerg4.marketplace.service.interfaces;

import java.util.UUID;

public interface IMailService {
    void sendConfirmSignupLetter(String email, String firstName, String lastName, UUID token);
}
