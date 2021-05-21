package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.dto.user.SignupRequestDto;
import com.netcrackerg4.marketplace.model.dto.user.UserUpdateDto;
import com.netcrackerg4.marketplace.model.enums.AccountActivation;
import com.netcrackerg4.marketplace.model.enums.UserRole;
import com.netcrackerg4.marketplace.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/staff")
@RequiredArgsConstructor
public class StaffController {
    private final IUserService userService;
    private final IUserService staffService;

    @PostMapping("/courier")
    public void createCourier(@RequestBody SignupRequestDto courierSignup) {
        userService.signupUser(courierSignup, AccountActivation.PASSWORD_RESET, UserRole.ROLE_COURIER);
    }

    @PostMapping("/product-manager")
    public void createManager(@RequestBody SignupRequestDto mgrSignup) {
        userService.signupUser(mgrSignup, AccountActivation.PASSWORD_RESET, UserRole.ROLE_PRODUCT_MGR);
    }

    @PutMapping("/edit")
    public void updateStaff(@Valid @RequestBody UserUpdateDto userUpdateDto) {
        staffService.updateUser(userUpdateDto);
    }
}
