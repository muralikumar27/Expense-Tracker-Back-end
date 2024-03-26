package com.murali.expensetracker.service;

import com.murali.expensetracker.entity.JwtToken;
import com.murali.expensetracker.repository.JwtRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LogoutService implements LogoutHandler {
    @Autowired
    private JwtRepository jwtRepository;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        String jwt;
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);

            Optional<JwtToken> optionalJwtToken = jwtRepository.findByToken(jwt);
            if(optionalJwtToken.isPresent()){
                JwtToken token = optionalJwtToken.get();
                token.setRevoked(true);
                jwtRepository.save(token);
            }
        }
    }
}
