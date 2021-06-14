package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.dto.auction.AuctionDto;

public interface IAuctionService {
    void create(AuctionDto obj);
    String getTypeNameById(int id);
}
