package com.netcrackerg4.marketplace.repository.interfaces;


import com.netcrackerg4.marketplace.model.domain.auction.AuctionTypeEntity;
import com.netcrackerg4.marketplace.model.dto.auction.AuctionDto;

import java.util.List;
import java.util.Optional;

public interface IAuctionDao {
     void create(AuctionDto obj);
     Optional<String> getTypeNameById(int id);
     List<AuctionTypeEntity> getTypes();
}
