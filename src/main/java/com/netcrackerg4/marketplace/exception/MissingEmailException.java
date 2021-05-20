package com.netcrackerg4.marketplace.exception;

public class MissingEmailException extends RuntimeException {
    private final String email;

    public MissingEmailException(String email) {
        this.email = email;
    }

    @Override
    public String getMessage() {
        return String.format("Could not find email '%s'.", email);
    }
}
