package com.netcrackerg4.marketplace.repository.interfaces;


import com.netcrackerg4.marketplace.model.dto.auction.AuctionDto;

import java.util.Optional;

public interface IAuctionDao {
     void create(AuctionDto obj);
     Optional<String> getTypeNameById(int id);
}
