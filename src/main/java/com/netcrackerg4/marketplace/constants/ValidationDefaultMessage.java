package com.netcrackerg4.marketplace.constants;

public class ValidationDefaultMessage {
    public static final String WRONG_FORMAT_EMAIL = "Wrong email format";
    public static final String WRONG_FORMAT_PASSWORD =
            "Password has to contain at least one capital letter, lower case letter and number";
    public static final String WRONG_FORMAT_LAST_NAME =
            "The last name must be of size between 2 and 30";
    public static final String WRONG_FORMAT_FIRST_NAME =
            "The first name must be of size between 2 and 30";
    public static final String WRONG_FORMAT_NAMING =
            "The name must be of size between 2 and 30, start with 2 characters, contain only characters, spaces and numbers";
    public static final String WRONG_FORMAT_PHONE_NUMBER =
            "The phone number must contain from 12 to 16 digits only.";
    public static final String WRONG_FORMAT_TIME_TO_BID =
            "Minimum time to bid is 10 seconds";
    public static final String WRONG_FORMAT_MIN_RISE =
            "Minimum bid rise is 5$";
    public static final String WRONG_FORMAT_LOWERING_STEP =
            "Minimum lowering step is 5 cents";
    public static final String WRONG_FORMAT_STEP_PERIOD =
            "Minimum step period is 60 seconds";
    public static final String WRONG_FORMAT_NUM_STEPS =
            "Minimum number of steps is 1";
    public static final String WRONG_FORMAT_AUCTION_STARTS_AT =
            "Auction must start in the future";
    public static final String WRONG_FORMAT_AUCTION_START_PRICE =
            "Auction must start at price at least 100$";
    public static final String WRONG_FORMAT_AUCTION_PRODUCT_QUANTITY =
            "Auction must offer more than 3 items of the given product";
}
