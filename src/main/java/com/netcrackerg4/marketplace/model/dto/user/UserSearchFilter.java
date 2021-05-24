package com.netcrackerg4.marketplace.model.dto.user;

import com.netcrackerg4.marketplace.model.enums.UserRole;
import com.netcrackerg4.marketplace.model.enums.UserStatus;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UserSearchFilter {
    @Nullable
    @Size(min = 1)
    private List<UserRole> targetRoles;
    @Nullable
    @Size(min = 1)
    private List<UserStatus> targetStatuses;
    @Nullable
    @Size(min = 2)
    private String firstNameSequence;
    @Nullable
    @Size(min = 2)
    private String lastNameSequence;
}
