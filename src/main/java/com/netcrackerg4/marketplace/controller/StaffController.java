package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.dto.user.*;
import com.netcrackerg4.marketplace.model.enums.AccountActivation;
import com.netcrackerg4.marketplace.model.enums.UserRole;
import com.netcrackerg4.marketplace.model.response.UserInfoResponse;
import com.netcrackerg4.marketplace.service.interfaces.IUserService;
import com.netcrackerg4.marketplace.util.EagerContentPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.*;

@RestController
@RequestMapping("/api/v1/admin/staff")
@RequiredArgsConstructor
public class StaffController {
    private final IUserService userService;
    private final Set<UserRole> staffRoles = new HashSet<>() {{
        add(UserRole.ROLE_COURIER);
        add(UserRole.ROLE_PRODUCT_MGR);
    }};

    @GetMapping("/{id}")
    public UserInfoResponse getMyProfile(@PathVariable("id") UUID stafferId) {
        return this.userService.getProfileById(stafferId);
    }

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
        userService.updateUser(userUpdateDto);
    }

    @PatchMapping("/change-status")
    public void updateStatus(@Valid @RequestBody ChangeStatusDto changeStatusDto) {
        userService.updateStatus(changeStatusDto);
    }

    @PostMapping("/find")
    EagerContentPage<UserAdminView> findEmployees(@RequestBody @Valid UserSearchFilter userFilter, @RequestParam @Min(0) int page) {
        if (userFilter.getTargetRoles() == null) {
            userFilter.setTargetRoles(List.of(UserRole.ROLE_COURIER, UserRole.ROLE_PRODUCT_MGR));
        } else if (!staffRoles.containsAll(userFilter.getTargetRoles())) {
            throw new IllegalStateException("Admin can only get access to staffers.");
        }
        return userService.findUsers(userFilter, page);
    }

    @GetMapping("/search-details")
    Map<String, List<String>> getAllRolesAndStatuses() {
        return userService.getAllRolesAndStatuses();
    }
}
