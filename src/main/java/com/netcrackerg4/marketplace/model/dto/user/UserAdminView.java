package com.netcrackerg4.marketplace.model.dto.user;

import com.netcrackerg4.marketplace.model.enums.UserRole;
import com.netcrackerg4.marketplace.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAdminView {
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    @Nullable
    private String phoneNumber;
    private UserStatus status;
    private UserRole role;
}
