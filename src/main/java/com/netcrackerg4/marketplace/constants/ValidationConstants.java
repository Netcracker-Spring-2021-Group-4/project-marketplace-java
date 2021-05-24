package com.netcrackerg4.marketplace.constants;

public class ValidationConstants {
    public static final String NAME_PATTERN = "\\p{L}{2,30}";
    public static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
    public static final String PHONE_PATTERN = "^[0-9]{10}$";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
}
