package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.service.interfaces.ICartService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public")
@AllArgsConstructor
public class ShoppingCartController {
    private final ICartService cartService;

    @GetMapping("/product/{id}/availability")
    public void checkInStockProduct(@PathVariable("id") UUID id, @RequestParam(name = "quantity") int quantity) {
        cartService.checkAvailability(id, quantity);
    }

    @GetMapping("/cart")
    public
}
