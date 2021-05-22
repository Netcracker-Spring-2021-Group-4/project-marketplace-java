package com.netcrackerg4.marketplace.model.dto.user;

import com.netcrackerg4.marketplace.constants.ValidationDefaultMessage;
import com.netcrackerg4.marketplace.model.enums.UserStatus;
import com.netcrackerg4.marketplace.validation.UserStatusTypeSubset;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class ChangeStatusDto {
    @NotNull
    @Email(message = ValidationDefaultMessage.WRONG_FORMAT_EMAIL)
    private String email;
    @NotNull
    @UserStatusTypeSubset(anyOf = {UserStatus.ACTIVE, UserStatus.INACTIVE, UserStatus.TERMINATED})
    private UserStatus userStatus;
}
