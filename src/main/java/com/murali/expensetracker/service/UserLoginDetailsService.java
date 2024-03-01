package com.murali.expensetracker.service;

import com.murali.expensetracker.details.UserInfoDetails;
import com.murali.expensetracker.entity.User;
import com.murali.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserLoginDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.getByEmailId(email);
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException("Invalid username in login");
        }
        User user = optionalUser.get();
        return new UserInfoDetails(user);
    }

    public UserDetails loadUserByUserId(long id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException("Try logging in again,Invalid JWT");
        }
        User user = optionalUser.get();
        return new UserInfoDetails(user);
    }
}
