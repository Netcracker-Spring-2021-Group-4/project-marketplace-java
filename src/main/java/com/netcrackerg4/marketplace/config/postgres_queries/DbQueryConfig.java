package com.netcrackerg4.marketplace.config.postgres_queries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        UserQueries.class,
        TokenQueries.class,
        UserRoleQueries.class,
        CartQueries.class,
        ProductQueries.class
})
public class DbQueryConfig {
    @Autowired
    private UserQueries postgresUserQueries;
    @Autowired
    private TokenQueries postgrestokenQueries;
    @Autowired
    private ProductQueries postgresProductQueries;
}
