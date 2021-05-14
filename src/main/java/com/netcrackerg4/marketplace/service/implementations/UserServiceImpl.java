package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.exception.ActivatedTokenReuseException;
import com.netcrackerg4.marketplace.exception.BadCodeError;
import com.netcrackerg4.marketplace.model.domain.AppUserEntity;
import com.netcrackerg4.marketplace.model.domain.TokenEntity;
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
                .ifPresent(user -> {throw new IllegalStateException("User with such email already exists.");});
        AppUserEntity userEntity = AppUserEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(signupRequest.getPlainPassword()))
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
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
    public void confirmSignup(String tokenValue) throws ActivatedTokenReuseException {
        UUID token = UUID.fromString(tokenValue);
        TokenEntity tokenEntity = tokenDao.read(token);
        if (tokenEntity.isActivated()) throw new ActivatedTokenReuseException();
        try {
            userDao.setStatus(tokenEntity.getUserEmail(), UserStatus.ACTIVE);
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
}
