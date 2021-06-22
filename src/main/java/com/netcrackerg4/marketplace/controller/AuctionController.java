package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.domain.auction.AuctionTypeEntity;
import com.netcrackerg4.marketplace.service.interfaces.IAuctionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth-store/auction")
@AllArgsConstructor
public class AuctionController {
    private final IAuctionService auctionService;

    @GetMapping("/types")
    public List<AuctionTypeEntity> getTypes() {
        return this.auctionService.getTypes();
    }
}
