package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.model.domain.user.AppUserEntity;
import com.netcrackerg4.marketplace.model.dto.ValidList;
import com.netcrackerg4.marketplace.model.dto.product.CartItemDto;
import com.netcrackerg4.marketplace.model.dto.user.UserUpdateDto;
import com.netcrackerg4.marketplace.model.enums.OrderStatus;
import com.netcrackerg4.marketplace.service.interfaces.ICartService;
import com.netcrackerg4.marketplace.service.interfaces.IOrderService;
import com.netcrackerg4.marketplace.service.interfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth-customer")
@AllArgsConstructor
public class AuthCustomerController {

    private final ICartService cartService;
    private final IUserService userService;
    private final IOrderService orderService;

    @PostMapping("/add-to-cart")
    public boolean addToCart(@Valid @RequestBody CartItemDto cartItemDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return cartService.addToCart(email, cartItemDto);
    }

    @PostMapping("/add-to-cart-if-possible")
    public void addToCartList(@Valid @RequestBody ValidList<CartItemDto> cartItemDto, Authentication auth){
        String email = auth.getName();
        cartService.addToCartListIfPossible(email, cartItemDto);
    }

    @PostMapping("/remove-from-cart")
    public boolean removeFromCart(@Valid @RequestBody CartItemDto cartItemDto, Authentication auth) {
        String email = auth.getName();
        return cartService.removeFromCart(email, cartItemDto);
    }

    @PutMapping("/me/edit")
    public void updateStaff(@Valid @RequestBody UserUpdateDto userUpdateDto) {
        userService.updateUser(userUpdateDto);
    }

    @PostMapping("/orders/cancelled/{orderId}")
    void cancelOrder(@PathVariable UUID orderId, Principal principal) {
        AppUserEntity customer = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalStateException("could not find such user"));
        if (!orderService.customerOwnsOrder(customer.getUserId(), orderId))
            throw new IllegalStateException("you did not make this order");
        orderService.setOrderStatus(orderId, OrderStatus.CANCELLED, true);
    }
}
