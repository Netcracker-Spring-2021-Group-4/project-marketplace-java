package com.netcrackerg4.marketplace.config.postgres_queries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "custom.postgres.user")
@ConstructorBinding
@AllArgsConstructor
@Getter
public class UserQueries {
    private final String getByEmail;
    private final String getAuthorities;
    private final String findUserById;
    private final String createNew;
    private final String updateStatus;
    private final String findStatusIdByName;
    private final String findRoleIdByName;
    private final String updateUserInfo;
    private final String updatePassword;
    private final String findByRoleStatusNames;
    private final String countByRoleStatusNames;
}
