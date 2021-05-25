package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.dto.product.CartItemDto;
import com.netcrackerg4.marketplace.service.interfaces.ICartService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth-customer")
@AllArgsConstructor
public class AuthCustomerController {

    private final ICartService cartService;

    @PostMapping("/add-to-cart")
    public void addToCard(@Valid @RequestBody CartItemDto cartItemDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        cartService.addToCart(email, cartItemDto);
    }
}
