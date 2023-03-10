package com.stelmach.bartosz.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getUserByNameAndPassword(String name, String password) {
        User user = userRepository.findByUsernameAndPassword(name, password);
        if(user == null){
            throw new IllegalArgumentException("Invalid id and password");
        }
        return user;
    }
}
