package com.netcrackerg4.marketplace.controller.cart;

import com.netcrackerg4.marketplace.model.dto.product.CartItemDto;
import com.netcrackerg4.marketplace.model.dto.ValidList;
import com.netcrackerg4.marketplace.model.response.CartInfoResponse;
import com.netcrackerg4.marketplace.service.interfaces.ICartService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public CartInfoResponse getMyCart(@Valid @RequestBody ValidList<CartItemDto> cartItems) {
        return cartService.getCartInfoNonAuthorized(cartItems);
    }

    @PostMapping("/reserve")
    public void setReservation(@Valid @RequestBody ValidList<CartItemDto> cartItems) {
        cartService.makeCartReservation(cartItems);
    }

    @PostMapping("/cancel-reservation")
    public void cancelReservation(@Valid @RequestBody ValidList<CartItemDto> cartItems) {
        cartService.cancelCartReservation(cartItems);
    }
}
