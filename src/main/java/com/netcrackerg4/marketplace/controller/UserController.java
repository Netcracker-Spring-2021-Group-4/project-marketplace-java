package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.dto.password.PasswordUpdateDto;
import com.netcrackerg4.marketplace.model.dto.password.PasswordWrapperDto;
import com.netcrackerg4.marketplace.model.dto.user.SignupRequestDto;
import com.netcrackerg4.marketplace.model.enums.AccountActivation;
import com.netcrackerg4.marketplace.model.enums.UserRole;
import com.netcrackerg4.marketplace.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/public/user")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @PostMapping("/request-signup")
    void requestSignup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        userService.signupUser(signupRequestDto, AccountActivation.EMAIL, UserRole.ROLE_CUSTOMER);
    }

    @PostMapping("/confirm-signup/{token}")
    void confirmSignup(@PathVariable String token) {
        userService.confirmSignup(token);
    }

    @PostMapping("/confirm-first-password/{token}")
    void confirmPasswordActivation(@PathVariable String token, @RequestBody PasswordWrapperDto passWrapper) {
        userService.confirmPasswordSignup(token, passWrapper.getPassword());
    }

    // request email for password reset
    @PostMapping("/password-reset/{email}")
    void requestResetPassword(@PathVariable String email) {
        userService.requestPasswordReset(email);
    }

    // check is the token passed in the reset email is still valid
    @GetMapping("/password-token-validity/{token}")
    void checkIsValid(@PathVariable String token) {
        userService.validatePasswordToken(token);
    }

    // reset password with confirmation email token
    @PatchMapping("/password/{token}")
    void resetPassword(@PathVariable String token, @RequestBody PasswordWrapperDto passWrapper) {
        userService.resetPassword(token, passWrapper.getPassword());
    }

    // change password without email confirmation
    @PatchMapping("/{email}/password")
    void changePassword(@RequestBody PasswordUpdateDto passwordData, @PathVariable String email) {
        userService.changePassword(email, passwordData);
    }
}
