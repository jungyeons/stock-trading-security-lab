package com.stocklab.service;

import com.stocklab.model.AppUser;
import com.stocklab.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AppUser findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("user not found"));
    }

    @Transactional
    public AppUser updateFullName(String username, String fullName) {
        AppUser user = findByUsername(username);
        user.setFullName(fullName);
        return user;
    }
}
