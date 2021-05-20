package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.dto.product.CartItemDto;
import com.netcrackerg4.marketplace.service.interfaces.ICartService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth-customer")
public class AuthCustomerController {

private final ICartService cartService ;

    public AuthCustomerController(ICartService cartService) {
        this.cartService = cartService;
    }


    @PostMapping("/add-to-card")
    public String addToCard(@Valid @RequestBody CartItemDto cardItem){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //cardItem.setCustomerId(authentication.);
        cardItem.setDateAdded(LocalDateTime.now());
         cartService.addToCart(cardItem);
         return "product was added";
    }
}
