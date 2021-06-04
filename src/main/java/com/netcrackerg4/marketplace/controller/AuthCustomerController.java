package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.dto.ValidList;
import com.netcrackerg4.marketplace.model.dto.product.CartItemDto;
import com.netcrackerg4.marketplace.model.dto.user.UserUpdateDto;
import com.netcrackerg4.marketplace.service.interfaces.ICartService;
import com.netcrackerg4.marketplace.service.interfaces.IUserService;
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
    private final IUserService userService;

    @PostMapping("/add-to-cart")
    public void addToCart(@Valid @RequestBody CartItemDto cartItemDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        cartService.addToCart(email, cartItemDto);
    }

    @PostMapping("/add-to-cart-if-possible")
    public void addToCart(@Valid @RequestBody ValidList<CartItemDto> cartItemDto, Authentication auth){
        String email = auth.getName();
        cartService.addToCartIfPossible(email, cartItemDto);
    }

    @PostMapping("/remove-from-cart")
    public void removeFromCart(@Valid @RequestBody CartItemDto cartItemDto, Authentication auth) {
        String email = auth.getName();
        cartService.removeFromCart(email, cartItemDto);
    }

    @PutMapping("/me/edit")
    public void updateStaff(@Valid @RequestBody UserUpdateDto userUpdateDto) {
        userService.updateUser(userUpdateDto);
    }
}
