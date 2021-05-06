package com.netcrackerg4.marketplace.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtUtil {

    @Value("${custom.jwt.secret}")
    private String SECRET_KEY;

    @Value("${custom.jwt.hours-valid}")
    private Integer HOURS_TOKEN_VALID;

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String token) {
        return extractClaim(token, claims -> {
            var sub = claims.getSubject();
            var authorities = (List<Map<String, String>>) claims.get("authorities");
            var set = authorities.stream()
                    .map(a -> new SimpleGrantedAuthority(a.get("authority")))
                    .collect(Collectors.toSet());
            return new UsernamePasswordAuthenticationToken(sub, null, set);
        });
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())).parseClaimsJws(token).getBody();
    }

    public String generateToken(Authentication authResults) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", authResults.getAuthorities());
        return createToken(claims, authResults.getName());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR_OF_DAY, HOURS_TOKEN_VALID);

        return Jwts.builder()
                .setClaims(claims).setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(calendar.getTime())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }
}
