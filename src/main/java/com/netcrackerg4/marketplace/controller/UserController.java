package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.dto.user.PasswordUpdateDto;
import com.netcrackerg4.marketplace.model.dto.user.SignupRequestDto;
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
        userService.signupUser(signupRequestDto, true, UserRole.ROLE_CUSTOMER);
    }

    @PostMapping("/confirm-signup/{token}")
    void confirmSignup(@PathVariable String token) {
        userService.confirmSignup(token);
    }

    // request email for password reset
    @GetMapping("/password-reset/{email}")
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
    void resetPassword(@PathVariable String token, @RequestParam CharSequence newPassword) {
        userService.resetPassword(token, newPassword);
    }

    // change password without email confirmation
    @PatchMapping("/{email}/password")
    void changePassword(@RequestBody PasswordUpdateDto passwordData, @PathVariable String email) {
        userService.changePassword(email, passwordData);
    }
}
