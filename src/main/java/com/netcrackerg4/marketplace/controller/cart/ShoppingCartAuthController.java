package com.netcrackerg4.marketplace.controller.cart;

import com.netcrackerg4.marketplace.model.response.CartInfoResponse;
import com.netcrackerg4.marketplace.service.interfaces.ICartService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth-customer")
@AllArgsConstructor
public class ShoppingCartAuthController {
    private final ICartService cartService;

    @GetMapping("/cart")
    public CartInfoResponse getMyCart(Authentication auth) {
        String email = auth.getName();
        return cartService.getCartInfoAuthorized(email);
    }

}
