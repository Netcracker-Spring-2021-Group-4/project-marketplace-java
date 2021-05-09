package com.netcrackerg4.marketplace.service.interfaces;

public interface IMailService {
    void sendConfirmSignupLetter(String email, String firstName, String lastName, String token);
}
