package com.netcrackerg4.marketplace.controller;

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

    @PostMapping("request-signup")
    void requestSignup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        userService.signupUser(signupRequestDto, true, UserRole.ROLE_CUSTOMER);
    }

    @PostMapping("confirm-signup/{token}")
    void confirmSignup(@PathVariable String token) {
        userService.confirmSignup(token);
    }
}
