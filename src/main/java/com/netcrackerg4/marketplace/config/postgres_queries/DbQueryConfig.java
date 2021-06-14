package com.netcrackerg4.marketplace.config.postgres_queries;

import com.netcrackerg4.marketplace.config.postgres_queries.order.AddressQueries;
import com.netcrackerg4.marketplace.config.postgres_queries.order.DeliverySlotQueries;
import com.netcrackerg4.marketplace.config.postgres_queries.order.OrderItemQueries;
import com.netcrackerg4.marketplace.config.postgres_queries.order.OrderQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        UserQueries.class,
        TokenQueries.class,
        CartQueries.class,
        ProductQueries.class,
        DiscountQueries.class,
        CategoryQueries.class,
        DiscountQueries.class,
        DeliverySlotQueries.class,
        OrderQueries.class,
        AddressQueries.class,
        OrderItemQueries.class,
        AdvLockQueries.class,
        AuctionQueries.class
})
public class DbQueryConfig {
    @Autowired
    private UserQueries postgresUserQueries;
    @Autowired
    private TokenQueries postgrestokenQueries;
    @Autowired
    private ProductQueries postgresProductQueries;
    @Autowired
    private CartQueries postgresCartQueries;
    @Autowired
    private CategoryQueries postgresCategoryQueries;
    @Autowired
    private CategoryQueries postgresAuctionQueries;
}
