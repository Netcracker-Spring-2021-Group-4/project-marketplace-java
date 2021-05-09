package com.netcrackerg4.marketplace.model.dto.user;

import com.netcrackerg4.marketplace.constants.ValidationConstants;
import com.netcrackerg4.marketplace.constants.ValidationDefaultMessage;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
public class SignupRequestDto {
    @Email(message= ValidationDefaultMessage.WRONG_FORMAT_EMAIL)
    private String email;
    @Pattern(
            regexp = ValidationConstants.PASSWORD_PATTERN,
            message= ValidationDefaultMessage.WRONG_FORMAT_PASSWORD
    )
    private CharSequence plainPassword;
    @Pattern(
            regexp = ValidationConstants.NAME_PATTERN,
            message = ValidationDefaultMessage.WRONG_FORMAT_FIRST_NAME
    )
    private String firstName;
    @Pattern(
            regexp = ValidationConstants.NAME_PATTERN,
            message = ValidationDefaultMessage.WRONG_FORMAT_LAST_NAME
    )
    private String lastName;
}
