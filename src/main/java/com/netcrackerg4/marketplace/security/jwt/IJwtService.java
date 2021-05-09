package com.netcrackerg4.marketplace.security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface IJwtService {
    String generateToken(Authentication authentication);

    String generateToken(Collection<GrantedAuthority> authorities, String subject);

    UsernamePasswordAuthenticationToken getUserPassAuthToken(String token); // 🙈
}
