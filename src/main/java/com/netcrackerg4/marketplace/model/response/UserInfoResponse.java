package com.netcrackerg4.marketplace.model.response;

import com.netcrackerg4.marketplace.model.domain.AppUserEntity;
import com.netcrackerg4.marketplace.model.enums.UserRole;
import com.netcrackerg4.marketplace.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class UserInfoResponse {
    private final UUID userId;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final UserRole role;
    private final UserStatus status;

    public UserInfoResponse(AppUserEntity userEntity) {
        this.userId = userEntity.getUserId();
        this.email = userEntity.getEmail();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.phoneNumber = userEntity.getPhoneNumber();
        this.role = userEntity.getRole();
        this.status = userEntity.getStatus();
    }
}
