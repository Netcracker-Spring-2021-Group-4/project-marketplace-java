package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.model.dto.auction.AuctionDto;
import com.netcrackerg4.marketplace.repository.interfaces.IAuctionDao;
import com.netcrackerg4.marketplace.service.interfaces.IAuctionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuctionServiceImpl implements IAuctionService {
    private final IAuctionDao auctionDao;

    @Override
    public void create(AuctionDto obj) {
        this.auctionDao.create(obj);
    }

    @Override
    public String getTypeNameById(int id) {
        return this.auctionDao.getTypeNameById(id)
                .orElseThrow(() -> new IllegalStateException("The type of the auction is not valid"));
    }
}
