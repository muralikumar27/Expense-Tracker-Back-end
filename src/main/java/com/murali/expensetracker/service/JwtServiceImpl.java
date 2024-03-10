package com.murali.expensetracker.service;

import com.murali.expensetracker.entity.User;
import com.murali.expensetracker.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

/**
 * This class is responsible for handling the
 * service required for JWT verification and
 * Authorization functions*/
@Service
public class JwtServiceImpl implements JwtService{
    @Autowired
    private UserRepository userRepository;

    @Value("${key}")
    private String SECRET;
    @Override
    public String generateToken(Long id) {
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,id);
    }
    private String createToken(Map<String, Object> claims, Long id) {
        return Jwts.builder()
                .claims(claims)
                .subject(String.valueOf(id))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*30))
                .signWith(getKey())
                .compact();

    }

    private SecretKey getKey() {
        byte[] key = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(key);
    }

    public String getUserId(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    public boolean verifyToken(String jwt){
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(getKey())
                .build();
        try {
            jwtParser.parse(jwt);
        } catch (Exception e) {
            return false;
        }
        return (!isTokenExpired(jwt));
    }



    @Override
    public User getUser(String email) {
        return userRepository.findByEmailId(email);
    }

    private Claims getAllClaimsFromToken(String token){
        Claims claims = null;
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(getKey())
                .build();
        try {
            claims =  jwtParser.parseSignedClaims(token).getPayload();
        }
        catch (Exception e){
            throw new JwtException("Something went wrong,Try logging in again");
        }
        return claims;
    }

    private Boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

}
