package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.response.UserInfoResponse;
import com.netcrackerg4.marketplace.service.interfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth-store")
@AllArgsConstructor
public class AuthStoreController {
    private final IUserService userService;

    @GetMapping("/me")
    public UserInfoResponse getMyProfile(Authentication auth) {
        String email = auth.getName();
        return this.userService.getProfileByEmail(email);
    }
}
