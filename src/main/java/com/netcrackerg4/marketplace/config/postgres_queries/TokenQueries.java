package com.netcrackerg4.marketplace.config.postgres_queries;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "custom.postgres.token")
public class TokenQueries {
    private String createToken;
    private String readToken;
}
