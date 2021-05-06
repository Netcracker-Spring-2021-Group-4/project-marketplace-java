package com.netcrackerg4.marketplace.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcrackerg4.marketplace.security.jwt.JwtUtil;
import com.netcrackerg4.marketplace.model.domain.UsernamePasswordDataObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            var usernamePasswordAuthRequest =
                    new ObjectMapper().readValue(request.getInputStream(), UsernamePasswordDataObject.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    usernamePasswordAuthRequest.getUsername(),
                    usernamePasswordAuthRequest.getPassword()
            );
            var res =  authenticationManager.authenticate(authentication);
            return res;
        } catch( IOException e ) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        var token = jwtUtil.generateToken(authResult);
        response.addHeader("Authorization", "Bearer " + token);
    }
}
