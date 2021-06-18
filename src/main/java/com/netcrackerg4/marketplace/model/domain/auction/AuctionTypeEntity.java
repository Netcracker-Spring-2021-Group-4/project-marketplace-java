package com.netcrackerg4.marketplace.model.domain.auction;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuctionTypeEntity {
    private int typeId;
    private String name;
}
