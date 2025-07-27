package com.spring.leila.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
                    "id", user.get().getId(),
                    "name", user.get().getName(),
                    "email", user.get().getEmail()));
        }
        return ResponseEntity.status(401).body("Credenciais inválidas");
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> request) {
        Optional<UserModel> optionalUser = userService.findByEmail(request.get("email"));
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }

        UserModel user = optionalUser.get();
        user.setName(request.get("name"));

        if (request.containsKey("password") && request.get("password") != null && !request.get("password").isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.get("password")));
        }

        userService.save(user);
        return ResponseEntity.ok(Map.of("message", "Perfil atualizado"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(400).body("Token não fornecido");
        }

        String tokenValue = token.startsWith("Bearer ") ? token.substring(7) : token;
        String emailFromToken = JwtUtil.extractEmail(tokenValue);
        if (emailFromToken == null) {
            return ResponseEntity.status(401).body("Token inválido");
        }

        Optional<UserModel> userOptional = userService.findByEmail(emailFromToken);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }

        UserModel user = userOptional.get();

        if (!user.getId().equals(id)) {
            return ResponseEntity.status(403).body("Você não tem permissão para excluir esta conta");
        }

        userService.delete(user);
        return ResponseEntity.ok(Map.of("message", "Conta excluída com sucesso"));
    }

}
