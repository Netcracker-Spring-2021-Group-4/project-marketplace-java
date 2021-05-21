package com.netcrackerg4.marketplace.model.dto.user;

import com.netcrackerg4.marketplace.model.enums.UserStatus;
import lombok.Data;

@Data
public class ChangeStatusDto {
    private String email;
    private UserStatus userStatus;
}
