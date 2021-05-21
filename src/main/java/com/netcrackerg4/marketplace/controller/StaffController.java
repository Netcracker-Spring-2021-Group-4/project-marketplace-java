package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.dto.user.ChangeStatusDto;
import com.netcrackerg4.marketplace.model.dto.user.UserUpdateDto;
import com.netcrackerg4.marketplace.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/staff")
@RequiredArgsConstructor
public class StaffController {
    private final IUserService staffService;

    @PutMapping("/edit")
    public void updateStaff(@Valid @RequestBody UserUpdateDto userUpdateDto) {
        staffService.updateUser(userUpdateDto);
    }

    @PatchMapping("/change-status")
    public void updateStatus(@Valid @RequestBody ChangeStatusDto changeStatusDto) {
        userService.updateStatus(changeStatusDto);
    }
}
