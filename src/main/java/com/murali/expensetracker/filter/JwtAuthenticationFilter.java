package com.murali.expensetracker.filter;


import com.murali.expensetracker.service.JwtService;
import com.murali.expensetracker.service.UserLoginDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

/**
 * This filter is used to stand in front of each
 * request for JWT based authorization for each request
 * doFilterInternal method will check the request url header
 * then verify the JWT ,if its valid then extract the user id
 * from the JWT and load the User details and set the securityContext
 * and pass the request to the next filters in the filter chain.*/
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;
    @Autowired
    UserLoginDetailsService userLoginDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (shouldSkipAuthentication(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader("Authorization");
        String jwt = null;
        long userId;
        if(authHeader != null && authHeader.startsWith("Bearer ")){
                jwt = authHeader.substring(7);
                if (jwtService.verifyToken(jwt)) {
                try {
                    String Id = jwtService.getUserId(jwt);
                    userId = Long.parseLong(Id);
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if(SecurityContextHolder.getContext().getAuthentication() == null){
                    UserDetails userDetails = userLoginDetailsService.loadUserByUserId(userId);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            else {
                throw new JwtException("Invalid JWT token");
            }
        }
        else {
            throw new JwtException("Invalid JWT token");
        }
        filterChain.doFilter(request,response);
    }
    private boolean shouldSkipAuthentication(HttpServletRequest request) {
        String path = request.getRequestURI();
        return Arrays.asList("/register", "/verify-user", "/forgot-password", "/reset-password",
                "/resend-token", "/auth").contains(path);
    }
}
