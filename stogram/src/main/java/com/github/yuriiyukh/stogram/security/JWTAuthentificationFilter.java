package com.github.yuriiyukh.stogram.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.yuriiyukh.stogram.entity.UserEntity;
import com.github.yuriiyukh.stogram.service.CustomUserDetailsService;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class JWTAuthentificationFilter extends OncePerRequestFilter {
    
    private JWTTokenProvider jwtTokenProvider;
    private CustomUserDetailsService customUserDetailsService;
    
    public JWTAuthentificationFilter(JWTTokenProvider jwtTokenProvider, CustomUserDetailsService customUserDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            String jwt = getJWTFromRequest(request);
            
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
                UserEntity userDetails = customUserDetailsService.loadUserById(userId);
                
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, Collections.emptyList());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            
        } catch (Exception ex) {
            log.error("Could not set user auth");
        }
        
        filterChain.doFilter(request, response);
        
    }
    
    private String getJWTFromRequest(HttpServletRequest request) {
        
        String bearToken = request.getHeader(SecurityConstants.HEADER_STRING);
        
        if (StringUtils.hasText(bearToken) && bearToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return bearToken.split(" ")[1];
        }
        return null;
    }

}
