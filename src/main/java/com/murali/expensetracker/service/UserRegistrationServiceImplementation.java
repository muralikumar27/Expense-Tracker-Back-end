package com.murali.expensetracker.service;


import com.murali.expensetracker.entity.User;
import com.murali.expensetracker.exception.UserAlreadyExistsException;
import com.murali.expensetracker.model.UserModel;
import com.murali.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationServiceImplementation implements UserRegistrationService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public void registerUser(UserModel userModel) throws Exception{
        User user = new User();
        user.setEmailId(userModel.getEmail());
        user.setName(userModel.getName());
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        user.setRole("user");
        if(userRepository.existsByEmailId(userModel.getEmail())){
            throw new UserAlreadyExistsException(userModel.getEmail()+" is already in use.");
        }
        userRepository.save(user);

    }


}
