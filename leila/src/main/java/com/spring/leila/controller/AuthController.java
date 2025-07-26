package com.spring.leila.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.leila.enums.Role;
import com.spring.leila.model.UserModel;
import com.spring.leila.security.JwtUtil;
import com.spring.leila.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        UserModel user = userService.registerUser(request.get("name"), request.get("email"), request.get("password"), Role.USER);
        return ResponseEntity.ok(user);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
       Optional<UserModel> user = userService.findByEmail(request.get("email"));
       if (user.isPresent() && user.get().getPassword().equals(request.get("password"))) {
         String token = JwtUtil.generateToken(user.get().getName());
         return ResponseEntity.ok(Map.of("token", token));
       }
       return ResponseEntity.status(401).body("Credenciais inválidas");
    }
}
