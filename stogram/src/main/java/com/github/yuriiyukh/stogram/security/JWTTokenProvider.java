package com.github.yuriiyukh.stogram.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.github.yuriiyukh.stogram.entity.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTTokenProvider {

    public String generateToken(Authentication authentication) {
        
        UserEntity user = (UserEntity) authentication.getPrincipal();

        String userId = Long.toString(user.getId());
        
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", user.getId());
        claimsMap.put("username", user.getUsername());
        claimsMap.put("firstname", user.getFirstName());
        claimsMap.put("lastname", user.getLastName());
        
        return Jwts.builder()
                .setSubject(userId)
                .addClaims(claimsMap)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .compact();
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
            .setSigningKey(SecurityConstants.SECRET)
            .parseClaimsJws(token);
            return true;
        } catch(MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            log.error(ex.getMessage());
            return false;
        }
    }
    
    public Long getUserIdFromToken(String token) {
        
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET)
                .parseClaimsJws(token)
                .getBody();
        
        String id = (String) claims.get("id");
        return Long.parseLong(id);
    }
    
}
