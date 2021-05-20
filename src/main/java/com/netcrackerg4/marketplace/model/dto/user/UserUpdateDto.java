package com.netcrackerg4.marketplace.model.dto.user;

import com.netcrackerg4.marketplace.constants.ValidationConstants;
import com.netcrackerg4.marketplace.constants.ValidationDefaultMessage;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

// if a field is provided, it is updated, else ignored
@Data
public class UserUpdateDto {
    @NotNull
    @Email(message = ValidationDefaultMessage.WRONG_FORMAT_EMAIL)
    private String email;

    @Nullable
    @Pattern(
            regexp = ValidationConstants.NAME_PATTERN,
            message = ValidationDefaultMessage.WRONG_FORMAT_FIRST_NAME
    )
    private String firstName;

    @Nullable
    @Pattern(
            regexp = ValidationConstants.NAME_PATTERN,
            message = ValidationDefaultMessage.WRONG_FORMAT_LAST_NAME
    )
    private String lastName;

    @Nullable
    @Pattern(
            regexp = ValidationConstants.PHONE_PATTERN,
            message = ValidationDefaultMessage.WRONG_FORMAT_PHONE_NUMBER
    )
    private String phoneNumber;
}
