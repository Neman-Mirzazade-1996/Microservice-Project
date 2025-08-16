package com.neman.springcloud.service;

import com.neman.springcloud.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtConfig jwtConfig;

    public String extractUsername(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String email = claims.get("email", String.class);
            log.debug("Extracted email from token: {}", email);
            return email;
        } catch (Exception e) {
            log.error("Error extracting email from token", e);
            return null;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(Map<String, Object> claims, String username) {
        return buildToken(claims, username, jwtConfig.getExpiration());
    }

    public String generateRefreshToken(String username) {
        return buildToken(new HashMap<>(), username, jwtConfig.getRefreshToken().getExpiration());
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            String username,
            long expiration
    ) {
        extraClaims.put("email", username);

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, String username) {
        try {
            final String tokenUsername = extractUsername(token);
            log.debug("Validating token for email: {}", tokenUsername);
            log.debug("Expected username: {}", username);

            if (tokenUsername == null || !tokenUsername.equals(username)) {
                log.debug("Username validation failed");
                return false;
            }

            if (isTokenExpired(token)) {
                log.debug("Token is expired");
                return false;
            }

            log.debug("Token is valid");
            return true;
        } catch (Exception e) {
            log.error("Error validating token", e);
            return false;
        }
    }

    public Mono<Boolean> isTokenValidReactive(String token, String username) {
        return Mono.fromCallable(() -> isTokenValid(token, username));
    }

    private boolean isTokenExpired(String token) {
        try {
            final Date expiration = extractExpiration(token);
            boolean isExpired = expiration.before(new Date());
            log.debug("Token expiration: {}, isExpired: {}", expiration, isExpired);
            return isExpired;
        } catch (Exception e) {
            log.error("Error checking token expiration", e);
            return true;
        }
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token.trim())
                    .getBody();
        } catch (Exception e) {
            log.error("Error parsing JWT token", e);
            throw new RuntimeException("Invalid JWT token: " + e.getMessage());
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
