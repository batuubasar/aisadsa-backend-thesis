package com.aisadsa.aisadsabackend.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    @Value("${spring.authentication.jwt.secret}")
    private String APP_SECRET;
    @Value("${spring.authentication.jwt.expires.in}")
    private long EXPIRES_IN;

    public String generateJwtToken(Authentication auth, UUID userId) {
        JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal();
        Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);

        return Jwts.builder().setClaims(Map.of("userId", userId)) // Embedding userId in jwtToken
                .setSubject(userDetails.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, APP_SECRET).compact();
    }

    UUID getUserIdFromJwt(String token) {
        Claims claims = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJwt(token).getBody();
        return UUID.fromString(claims.getSubject());
    }

    boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJwt(token);
            return !isTokenExpired(token);
        }
        catch (SignatureException | UnsupportedJwtException | IllegalArgumentException | ExpiredJwtException |
               MalformedJwtException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration =  Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJwt(token).getBody().getExpiration();
        return expiration.before(new Date());
    }
}
