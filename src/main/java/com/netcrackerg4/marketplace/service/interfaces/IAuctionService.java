package com.netcrackerg4.marketplace.service.interfaces;

import com.netcrackerg4.marketplace.model.domain.auction.AuctionTypeEntity;
import com.netcrackerg4.marketplace.model.dto.auction.AuctionDto;

import java.util.List;

public interface IAuctionService {
    void create(AuctionDto obj);
    String getTypeNameById(int id);
    List<AuctionTypeEntity> getTypes();
}
