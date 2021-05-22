package com.netcrackerg4.marketplace.validation;

import com.netcrackerg4.marketplace.model.enums.UserStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class UserStatusSubSetValidator implements ConstraintValidator<UserStatusTypeSubset, UserStatus> {
    private UserStatus[] subset;

    @Override
    public void initialize(UserStatusTypeSubset constraintAnnotation) {
        this.subset = constraintAnnotation.anyOf();
    }

    @Override
    public boolean isValid(UserStatus userStatus, ConstraintValidatorContext constraintValidatorContext) {
        return userStatus == null || Arrays.asList(subset).contains(userStatus);
    }
}
