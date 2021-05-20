package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.exception.BadCodeError;
import com.netcrackerg4.marketplace.exception.InvalidTokenException;
import com.netcrackerg4.marketplace.exception.MissingEmailException;
import com.netcrackerg4.marketplace.exception.WrongPasswordException;
import com.netcrackerg4.marketplace.model.domain.AppUserEntity;
import com.netcrackerg4.marketplace.model.domain.TokenEntity;
import com.netcrackerg4.marketplace.model.dto.user.PasswordUpdateDto;
import com.netcrackerg4.marketplace.model.dto.user.SignupRequestDto;
import com.netcrackerg4.marketplace.model.enums.UserRole;
import com.netcrackerg4.marketplace.model.enums.UserStatus;
import com.netcrackerg4.marketplace.repository.interfaces.IAuthDao;
import com.netcrackerg4.marketplace.repository.interfaces.ITokenDao;
import com.netcrackerg4.marketplace.repository.interfaces.IUserDao;
import com.netcrackerg4.marketplace.service.interfaces.IMailService;
import com.netcrackerg4.marketplace.service.interfaces.IUserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    @Value("${custom.jwt.hours-valid}")
    private final Integer HOURS_TOKEN_VALID;
    private final IUserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final IAuthDao authDao;
    private final IMailService mailService;
    private final ITokenDao tokenDao;

    // should return minimal data set to build UserDetails
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return findByEmail(s); // mock
    }

    @Transactional
    @Override
    public void signupUser(SignupRequestDto signupRequest, boolean withConfirmation, UserRole role) {
        int roleId = authDao.getRoleIdByName(role.name()).orElseThrow(BadCodeError::new);
        String email = signupRequest.getEmail();
        userDao.findByEmail(email)
                .ifPresent(user -> {
                    throw new IllegalStateException("User with such email already exists.");
                });
        AppUserEntity userEntity = AppUserEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(signupRequest.getPlainPassword()))
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .phoneNumber(signupRequest.getPhoneNumber())
                .status(withConfirmation ? UserStatus.UNCONFIRMED : UserStatus.ACTIVE)
                .roleId(roleId)
                .build();
        userDao.create(userEntity);
        if (withConfirmation) {
            UUID token = UUID.randomUUID();
            tokenDao.create(new TokenEntity(token, signupRequest.getEmail(),
                    Instant.now().plusSeconds(HOURS_TOKEN_VALID * 3600), false));
            mailService.sendConfirmSignupLetter(signupRequest.getEmail(), signupRequest.getFirstName(),
                    signupRequest.getLastName(), token);
        }
    }

    @Transactional
    @Override
    public void confirmSignup(String tokenValue) throws InvalidTokenException {
        UUID token = UUID.fromString(tokenValue);
        TokenEntity tokenEntity = tokenDao.read(token);
        if (tokenEntity.isActivated()) throw new InvalidTokenException("Attempt to reuse activated token.");
        try {
            userDao.updateStatus(tokenEntity.getUserEmail(), UserStatus.ACTIVE);
            tokenDao.setActivated(token);
        } catch (JwtException e) {
            throw new IllegalStateException(String.format("Token %s can not be trusted.", token));
        }
    }

    // should return fully initialized AppUserEntity
    @Override
    public AppUserEntity findByEmail(String email) {
        AppUserEntity user = userDao.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with such email not found."));
        user.setAuthorities(userDao.getAuthorities(user.getRoleId()));
        return user;
    }

    @Override
    public void requestPasswordReset(String email) {
        TokenEntity resetToken = new TokenEntity(UUID.randomUUID(), email,
                Instant.now().plusSeconds(HOURS_TOKEN_VALID * 3600), false);
        tokenDao.create(resetToken);
        mailService.sendPasswordResetEmail(email, resetToken);
    }

    @Override
    public void validatePasswordToken(String tokenValue) {
        TokenEntity token = tokenDao.read(UUID.fromString(tokenValue));
        doTokenValidation(token);
    }

    private void doTokenValidation(TokenEntity token) {
        if (token.isActivated()) throw new InvalidTokenException("Attempt to reuse activated token.");
        if (token.getExpiresAt().isBefore(Instant.now())) throw new InvalidTokenException("Token is already expired.");
    }

    @Override
    @Transactional
    public void resetPassword(String tokenValue, CharSequence newPassword) {
        TokenEntity token = tokenDao.read(UUID.fromString(tokenValue));
        doTokenValidation(token);
        String enpass = passwordEncoder.encode(newPassword);
        userDao.updatePassword(token.getUserEmail(), enpass);
        tokenDao.setActivated(token.getTokenValue());
    }

    @Override
    @Transactional
    public void changePassword(String email, PasswordUpdateDto passwordUpdateDto) {
        AppUserEntity userEntity = userDao.findByEmail(email).orElseThrow(() -> new MissingEmailException(email));
        if (!passwordEncoder.matches(passwordUpdateDto.getCurrentPassword(), userEntity.getPassword()))
            throw new WrongPasswordException();
        String enpass = passwordEncoder.encode(passwordUpdateDto.getNewPassword());
        userDao.updatePassword(email, enpass);
    }
}
