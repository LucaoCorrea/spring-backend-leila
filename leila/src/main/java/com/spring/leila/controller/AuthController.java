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
        UserModel user = userService.registerUser(request.get("name"), request.get("email"), request.get("password"),
                Role.USER);
        String token = JwtUtil.generateToken(user.getEmail(), Role.USER.name());
        return ResponseEntity.ok(Map.of("user", user, "token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        Optional<UserModel> user = userService.findByEmail(request.get("email"));

        if (user.isPresent() && passwordEncoder.matches(request.get("password"), user.get().getPassword())) {
            String token = JwtUtil.generateTokenLogin(user.get().getEmail(), user.get(), user.get().getRole().name());
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "role", user.get().getRole(),
                    "id", user.get().getId()));
        }
        return ResponseEntity.status(401).body("Credenciais inválidas");
    }
}
