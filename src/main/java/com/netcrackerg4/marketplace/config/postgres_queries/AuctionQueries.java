package com.netcrackerg4.marketplace.config.postgres_queries;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "custom.postgres.auction")
public class AuctionQueries {
    private String create;
    private String fetchTypeById;
    private String fetchTypes;
}
