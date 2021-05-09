package com.netcrackerg4.marketplace.model.dto.user;

import com.netcrackerg4.marketplace.constants.ValidationConstants;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
public class SignupRequestDto {
    @Email
    private String email;
    private CharSequence plainPassword;
    @Pattern(regexp = ValidationConstants.NAME_PATTERN)
    private String firstName;
    @Pattern(regexp = ValidationConstants.NAME_PATTERN)
    private String lastName;
}
