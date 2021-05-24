package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.dto.product.CartItemDto;
import com.netcrackerg4.marketplace.model.dto.product.ProductDto;
import com.netcrackerg4.marketplace.service.interfaces.ICartService;
import com.netcrackerg4.marketplace.service.interfaces.IProductService;
import com.netcrackerg4.marketplace.service.interfaces.IUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth-customer")
public class AuthCustomerController {

private final ICartService cartService ;
private final IUserService userService;

private final IProductService productService;

    public AuthCustomerController(ICartService cartService, IUserService userService, IProductService productService) {
        this.cartService = cartService;
        this.userService = userService;
        this.productService = productService;
    }


    @PostMapping("/add-to-cart")
    public String addToCard(@Valid @RequestBody CartItemDto cardItem){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        cardItem.setCustomerId(userService.findByEmail(email).getUserId());

        return cartService.addToCart(cardItem);
    }



}
