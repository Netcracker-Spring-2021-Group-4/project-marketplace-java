package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.exception.InvalidTokenException;
import com.netcrackerg4.marketplace.model.domain.user.AppUserEntity;
import com.netcrackerg4.marketplace.model.dto.password.PasswordUpdateDto;
import com.netcrackerg4.marketplace.model.dto.user.*;
import com.netcrackerg4.marketplace.model.enums.AccountActivation;
import com.netcrackerg4.marketplace.model.enums.UserRole;
import com.netcrackerg4.marketplace.model.response.UserInfoResponse;
import com.netcrackerg4.marketplace.util.EagerContentPage;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface IUserService extends UserDetailsService {
    void signupUser(SignupRequestDto signupRequest, AccountActivation activationType, UserRole role);

    void confirmSignup(String token);

    Optional<AppUserEntity> findByEmail(String email);

    void updateUser(UserUpdateDto staffUpdate);

    void updateStatus(ChangeStatusDto changeStatus);

    void requestPasswordReset(String email);

    void validatePasswordToken(String token) throws InvalidTokenException;

    void resetPassword(String token, CharSequence newPassword);

    void changePassword(String email, PasswordUpdateDto passwordUpdateDto);

    void confirmPasswordSignup(String tokenValue, CharSequence newPassword);

    EagerContentPage<UserAdminView> findUsers(UserSearchFilter searchFilter, int page);

    Map<String, List<String>> getAllRolesAndStatuses();
    UserInfoResponse getProfileByEmail(String email);
    UserInfoResponse getProfileById(UUID id);
}
