package com.netcrackerg4.marketplace.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    PostgresUserQueries.class
})
public class DbQueryConfig {
    @Autowired
    private PostgresUserQueries postgresUserQueries;
}
