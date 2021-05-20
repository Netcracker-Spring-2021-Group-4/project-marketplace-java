package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.exception.InvalidTokenException;
import com.netcrackerg4.marketplace.model.domain.AppUserEntity;
import com.netcrackerg4.marketplace.model.dto.user.PasswordUpdateDto;
import com.netcrackerg4.marketplace.model.dto.user.SignupRequestDto;
import com.netcrackerg4.marketplace.model.enums.UserRole;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    void signupUser(SignupRequestDto signupRequest, boolean withConfirmation, UserRole role);

    void confirmSignup(String token);

    AppUserEntity findByEmail(String email);

    void requestPasswordReset(String email);

    void validatePasswordToken(String token) throws InvalidTokenException;

    void resetPassword(String token, CharSequence newPassword);

    void changePassword(String email, PasswordUpdateDto passwordUpdateDto);
}
