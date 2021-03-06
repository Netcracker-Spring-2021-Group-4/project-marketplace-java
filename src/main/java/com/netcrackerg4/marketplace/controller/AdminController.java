package com.netcrackerg4.marketplace.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/admin")
@AllArgsConstructor
public class AdminController {

    @GetMapping("/demo")
    public Collection<? extends GrantedAuthority> demo(Authentication authentication) {
        return authentication.getAuthorities();
    }



}
