package com.netcrackerg4.marketplace.model.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.UUID;

@Data
@NoArgsConstructor
public class UserCoreView {
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    @Nullable
    private String phoneNumber;
}
