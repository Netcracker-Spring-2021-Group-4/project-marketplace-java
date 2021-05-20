package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.dto.user.SignupRequestDto;
import com.netcrackerg4.marketplace.model.enums.UserRole;
import com.netcrackerg4.marketplace.service.interfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/admin")
@AllArgsConstructor
public class AdminController {
    private final IUserService userService;

    @GetMapping("/demo")
    public Collection<? extends GrantedAuthority> demo(Authentication authentication) {
        return authentication.getAuthorities();
    }

    @PostMapping("/staff/courier")
    public void createCourier(@RequestBody SignupRequestDto courierSignup) {
        userService.signupUser(courierSignup, true, UserRole.ROLE_COURIER);
    }

    @PostMapping("/staff/product-manager")
    public void createManager(@RequestBody SignupRequestDto mgrSignup) {
        userService.signupUser(mgrSignup, true, UserRole.ROLE_PRODUCT_MGR);
    }

}
