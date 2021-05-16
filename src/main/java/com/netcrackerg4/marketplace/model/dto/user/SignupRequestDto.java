package com.netcrackerg4.marketplace.model.dto.user;

import com.netcrackerg4.marketplace.constants.ValidationConstants;
import com.netcrackerg4.marketplace.constants.ValidationDefaultMessage;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class SignupRequestDto {
    @NotNull
    @Email(message = ValidationDefaultMessage.WRONG_FORMAT_EMAIL)
    private String email;
    @NotNull
    @Pattern(
            regexp = ValidationConstants.PASSWORD_PATTERN,
            message = ValidationDefaultMessage.WRONG_FORMAT_PASSWORD
    )
    private CharSequence plainPassword;
    @NotNull
    @Pattern(
            regexp = ValidationConstants.NAME_PATTERN,
            message = ValidationDefaultMessage.WRONG_FORMAT_FIRST_NAME
    )
    private String firstName;
    @NotNull
    @Pattern(
            regexp = ValidationConstants.NAME_PATTERN,
            message = ValidationDefaultMessage.WRONG_FORMAT_LAST_NAME
    )
    private String lastName;

    @Pattern(
            regexp = ValidationConstants.PHONE_PATTERN,
            message = ValidationDefaultMessage.WRONG_FORMAT_PHONE_NUMBER
    )
    private String phoneNumber;
}
