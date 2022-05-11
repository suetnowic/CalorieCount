package com.viktorsuetnov.caloriecounting.security.jwt;

import com.viktorsuetnov.caloriecounting.model.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.viktorsuetnov.caloriecounting.security.SecurityConstants.*;

@Component
public class JwtTokenProvider {

    public static final Logger LOG = LoggerFactory.getLogger(JwtTokenProvider.class);

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expDate = new Date(now.getTime() + JWT_EXPIRATION_TIME);

        String userId = Long.toString(user.getId());

        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", userId);
        claimsMap.put("username", user.getEmail());
        return Jwts.builder()
                .setSubject(userId)
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(expDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException signatureException) {
            LOG.error("Invalid JWT signature: {}", signatureException.getMessage());
        } catch (MalformedJwtException malformedJwtException) {
            LOG.error("Invalid JWT token {}", malformedJwtException.getMessage());
        } catch (ExpiredJwtException expiredJwtException) {
            LOG.error("JWT token is expired: {}", expiredJwtException.getMessage());
        } catch (UnsupportedJwtException unsupportedJwtException) {
            LOG.error("JWT token is unsupported: {}", unsupportedJwtException.getMessage());
        } catch (IllegalArgumentException illegalArgumentException) {
            LOG.error("JWT claims string is empty: {}", illegalArgumentException.getMessage());
        }
        return false;
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        String id = (String) claims.get("id");
        return Long.parseLong(id);
    }
}
