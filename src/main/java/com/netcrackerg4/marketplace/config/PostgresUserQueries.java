package com.netcrackerg4.marketplace.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "custom.postgres.user")
public class PostgresUserQueries {
    private String getByEmail;
    private String getAuthorities;
}
