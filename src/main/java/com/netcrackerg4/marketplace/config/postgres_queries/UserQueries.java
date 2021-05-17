package com.netcrackerg4.marketplace.config.postgres_queries;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "custom.postgres.user")
public class UserQueries {
    private String getByEmail;
    private String getAuthorities;
    private String findUserById;
    private String createNew;
    private String updateStatus;
    private String updatePersonInfo;
    private String findStatusIdByName;
}
