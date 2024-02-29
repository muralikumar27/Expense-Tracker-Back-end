package com.murali.expensetracker.service;

import com.murali.expensetracker.entity.User;
import com.murali.expensetracker.model.LoginModel;
import com.murali.expensetracker.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

@Service
public class JwtServiceImpl implements JwtService{
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Value("${key}")
    private String SECRET;
    @Override
    public String generateToken(Long id) {
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,id);
    }

    /**
     * Handles user validation process by verifying the
     * emailId and password entered by the user
     * @param loginModel contains emailId and password
     * @return User object for creating JWT based on userId
     * */
    @Override
    public User verifyUser(LoginModel loginModel) {

        Optional<User> optionalUser = userRepository.getByEmailId(loginModel.getEmail());
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException("Invalid email for login");
        }
        User user = optionalUser.get();
        if(!passwordEncoder.matches(loginModel.getPassword(), user.getPassword())){
            throw new BadCredentialsException("wrong password");
        }
        return user;
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

    private Key getKey() {
        byte[] key = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(key);
    }


}
