package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.dto.user.SignupRequestDto;
import com.netcrackerg4.marketplace.model.dto.user.StaffUpdateDto;
import com.netcrackerg4.marketplace.service.interfaces.IStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
     private final IStaffService staffService;

    @GetMapping("/demo")
    public Collection<? extends GrantedAuthority> demo(Authentication authentication) {
        return authentication.getAuthorities();
    }

    @PutMapping("/sttafs/edit")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void updateStaff(@Valid @RequestBody StaffUpdateDto staffUpdateDto){
        staffService.updateUser(staffUpdateDto);
    }

}
