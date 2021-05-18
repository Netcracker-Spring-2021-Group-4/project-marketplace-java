package com.netcrackerg4.marketplace.controller;
import com.netcrackerg4.marketplace.model.dto.user.StaffUpdateDto;
import com.netcrackerg4.marketplace.service.interfaces.IStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/staff")
@RequiredArgsConstructor
public class AdminStaffManipulationController {
    private final IStaffService staffService;

    @PutMapping("/edit")
        public void updateStaff(@Valid @RequestBody StaffUpdateDto staffUpdateDto){
        staffService.updateUser(staffUpdateDto);
    }
}
