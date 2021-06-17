package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.model.domain.auction.AuctionTypeEntity;
import com.netcrackerg4.marketplace.model.dto.auction.AuctionDto;
import com.netcrackerg4.marketplace.model.dto.product.CartItemDto;
import com.netcrackerg4.marketplace.repository.interfaces.IAuctionDao;
import com.netcrackerg4.marketplace.service.interfaces.IAuctionService;
import com.netcrackerg4.marketplace.service.interfaces.ICartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class AuctionServiceImpl implements IAuctionService {
    private final IAuctionDao auctionDao;
    private final ICartService cartService;

    @Override
    public void create(AuctionDto obj) {
        cartService.makeCartReservation(Arrays.asList(new CartItemDto(obj.getProductQuantity(), obj.getProductId())));
        this.auctionDao.create(obj);
    }

    @Override
    public String getTypeNameById(int id) {
        return this.auctionDao.getTypeNameById(id)
                .orElseThrow(() -> new IllegalStateException("The type of the auction is not valid"));
    }

    @Override
    public List<AuctionTypeEntity> getTypes() {
        return this.auctionDao.getTypes();
    }
}
