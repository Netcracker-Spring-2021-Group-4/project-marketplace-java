package com.netcrackerg4.marketplace.model.dto.password;

import com.netcrackerg4.marketplace.constants.ValidationConstants;
import com.netcrackerg4.marketplace.constants.ValidationDefaultMessage;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class PasswordUpdateDto {
    // no need to check already existing password that is about to be changed
    private CharSequence currentPassword;
    @Pattern(
            regexp = ValidationConstants.PASSWORD_PATTERN,
            message = ValidationDefaultMessage.WRONG_FORMAT_PASSWORD
    )
    private CharSequence newPassword;
}
