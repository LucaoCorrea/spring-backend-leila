package com.spring.leila.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.leila.enums.Role;
import com.spring.leila.model.UserModel;
import com.spring.leila.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserModel registerUser(String name, String email, String password, Role role) {
        Optional<UserModel> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        String encryptedPassword = passwordEncoder.encode(password);

        UserModel user = new UserModel();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(encryptedPassword);
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
