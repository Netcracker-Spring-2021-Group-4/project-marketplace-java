package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.exception.InvalidTokenException;
import com.netcrackerg4.marketplace.model.domain.user.AppUserEntity;
import com.netcrackerg4.marketplace.model.domain.user.TokenEntity;
import com.netcrackerg4.marketplace.model.dto.password.PasswordUpdateDto;
import com.netcrackerg4.marketplace.model.dto.user.*;
import com.netcrackerg4.marketplace.model.enums.AccountActivation;
import com.netcrackerg4.marketplace.model.enums.UserRole;
import com.netcrackerg4.marketplace.model.enums.UserStatus;
import com.netcrackerg4.marketplace.model.response.UserInfoResponse;
import com.netcrackerg4.marketplace.repository.interfaces.ITokenDao;
import com.netcrackerg4.marketplace.repository.interfaces.IUserDao;
import com.netcrackerg4.marketplace.service.interfaces.IMailService;
import com.netcrackerg4.marketplace.service.interfaces.IUserService;
import com.netcrackerg4.marketplace.util.EagerContentPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    @Value("${custom.jwt.hours-valid}")
    private final Integer HOURS_TOKEN_VALID;
    @Value("${custom.pagination.user-search}")
    private final int SEARCH_PAGE_SIZE;
    private final IUserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final IMailService mailService;
    private final ITokenDao tokenDao;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return findByEmail(s).orElseThrow(() -> new IllegalStateException("User with such email not found.")); // mock
    }

    @Transactional
    @Override
    public void signupUser(SignupRequestDto signupRequest, AccountActivation activationType, UserRole role) {
        String email = signupRequest.getEmail();
        if (userDao.findByEmail(email).isPresent())
            throw new IllegalStateException("User with such email already exists.");
        AppUserEntity userEntity = AppUserEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(signupRequest.getPlainPassword()))
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .phoneNumber(signupRequest.getPhoneNumber())
                .status(activationType == AccountActivation.NONE ? UserStatus.ACTIVE : UserStatus.UNCONFIRMED)
                .role(role)
                .build();
        userDao.create(userEntity);

        if (activationType != AccountActivation.NONE) {
            UUID token = UUID.randomUUID();
            tokenDao.create(new TokenEntity(token, signupRequest.getEmail(),
                    Instant.now().plusSeconds(HOURS_TOKEN_VALID * 3600), false, activationType == AccountActivation.PASSWORD_RESET));
            mailService.sendConfirmSignupLetter(signupRequest.getEmail(), signupRequest.getFirstName(),
                    signupRequest.getLastName(), token, activationType);
        }
    }

    @Transactional
    @Override
    public void confirmSignup(String tokenValue) throws InvalidTokenException {
        UUID token = UUID.fromString(tokenValue);
        TokenEntity tokenEntity = tokenDao.read(token).orElseThrow();
        if (tokenEntity.isActivated()) {
            throw new InvalidTokenException("Attempt to reuse activated token.");
        }
        if (tokenEntity.isForPassword()) {
            throw new InvalidTokenException("Token meant for password change cannot be user for account activation.");
        }
        userDao.updateStatus(tokenEntity.getUserEmail(), UserStatus.ACTIVE);
        tokenDao.setActivated(token);
    }

    @Override
    public Optional<AppUserEntity> findByEmail(String email) {
        Optional<AppUserEntity> maybeUser = userDao.findByEmail(email);
        maybeUser.ifPresent(user ->
                user.setAuthorities(userDao.getAuthorities(userDao.findRoleIdByRoleName(user.getRole().name()))));
        return maybeUser;
    }

    @Transactional
    @Override
    public void updateStatus(ChangeStatusDto changeStatus) {
        String email = changeStatus.getEmail();
        userDao.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with such email not found."));
        userDao.updateStatus(email, changeStatus.getUserStatus());
    }

    @Override
    public void requestPasswordReset(String email) {
        if (userDao.findByEmail(email).isEmpty())
            throw new IllegalStateException("There is no user with such email: " + email);
        TokenEntity resetToken = new TokenEntity(UUID.randomUUID(), email,
                Instant.now().plusSeconds(HOURS_TOKEN_VALID * 3600), false, true);
        tokenDao.create(resetToken);
        mailService.sendPasswordResetEmail(email, resetToken.getTokenValue());
    }

    @Override
    public void validatePasswordToken(String tokenValue) {
        TokenEntity token = tokenDao.read(UUID.fromString(tokenValue)).orElseThrow(() -> new InvalidTokenException("Token not found."));
        doTokenValidation(token);
    }

    @Override
    @Transactional
    public void resetPassword(String tokenValue, CharSequence newPassword) {
        doResetPassword(tokenValue, newPassword);
    }

    private void doTokenValidation(TokenEntity token) {
        if (token.isActivated()) throw new InvalidTokenException("Attempt to reuse activated token.");
        if (token.getExpiresAt().isBefore(Instant.now())) throw new InvalidTokenException("Token is already expired.");
        if (!token.isForPassword()) throw new InvalidTokenException("Token not meant for password change.");
    }

    @Override
    @Transactional
    public void confirmPasswordSignup(String tokenValue, CharSequence newPassword) {
        String userEmail = doResetPassword(tokenValue, newPassword);
        userDao.updateStatus(userEmail, UserStatus.ACTIVE);
    }

    @Override
    public EagerContentPage<UserAdminView> findUsers(UserSearchFilter searchFilter, int page) {
        List<UserRole> roles = searchFilter.getTargetRoles() != null ? searchFilter.getTargetRoles() : List.of(UserRole.values());
        List<UserStatus> statuses = searchFilter.getTargetStatuses() != null ? searchFilter.getTargetStatuses() : List.of(UserStatus.values());
        String firstName = searchFilter.getFirstNameSeq() != null ? searchFilter.getFirstNameSeq() : "";
        String lastName = searchFilter.getLastNameSeq() != null ? searchFilter.getLastNameSeq() : "";
        List<UserAdminView> content = userDao.findUsersByFilter(roles, statuses, firstName, lastName, SEARCH_PAGE_SIZE, page);
        int numPages = userDao.countFilteredUsers(roles, statuses, firstName, lastName);
        return new EagerContentPage<>(content, numPages, SEARCH_PAGE_SIZE);
    }

    @Override
    public Map<String, List<String>> getAllRolesAndStatuses() {
        return new HashMap<>() {{
            put("roles", Arrays.stream(UserRole.values()).map(UserRole::toString).collect(Collectors.toList()));
            put("statuses", Arrays.stream(UserStatus.values()).map(UserStatus::toString).collect(Collectors.toList()));
        }};
    }

    @Override
    @Transactional
    public void changePassword(String email, PasswordUpdateDto passwordUpdateDto) {
        AppUserEntity userEntity = userDao.findByEmail(email).orElseThrow(() ->
                new IllegalStateException("There is no user with such email:" + email));
        if (!passwordEncoder.matches(passwordUpdateDto.getCurrentPassword(), userEntity.getPassword()))
            throw new IllegalStateException("Your current password is filled wrong");
        if (passwordUpdateDto.getCurrentPassword().equals(passwordUpdateDto.getNewPassword()))
            throw new IllegalStateException("Your previous password is the same");
        String enpass = passwordEncoder.encode(passwordUpdateDto.getNewPassword());
        userDao.updatePassword(email, enpass);
    }


    @Transactional
    @Override
    public void updateUser(UserUpdateDto staffUpdate) {
        String email = staffUpdate.getEmail();

        AppUserEntity userEntity = AppUserEntity.builder()
                .email(email)
                .firstName(staffUpdate.getFirstName())
                .lastName(staffUpdate.getLastName())
                .phoneNumber(staffUpdate.getPhoneNumber())
                .build();
        userDao.update(userEntity);
    }

    private String doResetPassword(String tokenValue, CharSequence newPassword) {
        TokenEntity token = tokenDao.read(UUID.fromString(tokenValue)).orElseThrow();
        doTokenValidation(token);
        AppUserEntity userEntity = userDao.findByEmail(token.getUserEmail())
                .orElseThrow(() -> new IllegalStateException("User with such email not found."));
        String oldEnpass = userEntity.getPassword();
        if (passwordEncoder.matches(newPassword, oldEnpass))
            throw new IllegalStateException("Your previous password is the same");
        String enpass = passwordEncoder.encode(newPassword);
        userDao.updatePassword(token.getUserEmail(), enpass);
        tokenDao.setActivated(token.getTokenValue());
        return userEntity.getEmail();
    }

    @Override
    public UserInfoResponse getProfileByEmail(String email) {
        AppUserEntity appUserEntity = userDao.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User with such email not found."));
        return new UserInfoResponse(appUserEntity);
    }

    @Override
    public UserInfoResponse getProfileById(UUID id) {
        AppUserEntity appUserEntity = userDao.read(id)
                .orElseThrow(() -> new IllegalStateException(String.format("User with id %s not found.", id)));
        return new UserInfoResponse(appUserEntity);
    }
}

