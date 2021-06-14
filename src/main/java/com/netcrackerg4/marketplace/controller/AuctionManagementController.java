package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.dto.auction.AuctionDto;
import com.netcrackerg4.marketplace.service.interfaces.IAuctionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/manager/auction")
@AllArgsConstructor
public class AuctionManagementController {
    private final IAuctionService auctionService;

    @PostMapping
    public void createAuction(@Valid @RequestBody AuctionDto auctionDto) {
        this.auctionService.create(auctionDto);
    }
}
