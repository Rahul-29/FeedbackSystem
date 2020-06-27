package com.feedback.poc.loginservice.service;

import com.feedback.poc.loginservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TokenValidationService {

    @Autowired
    private AppUserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    public String validateToken(final String authorizationHeader){
        String username = null;
        String jwt = null;

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }
        if(username != null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if(jwtUtil.validateToken(jwt, userDetails)){
                return username;
            }
        }
        return username;
    }
}
