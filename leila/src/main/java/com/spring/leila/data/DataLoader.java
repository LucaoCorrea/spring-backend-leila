package com.spring.leila.data;

import com.spring.leila.enums.Role;
import com.spring.leila.model.UserModel;
import com.spring.leila.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserService userService;

    public DataLoader(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userService.findByEmail("admin@gmail.com").isEmpty()) {
            UserModel admin = new UserModel();
            admin.setName("admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword("admin"); 
            admin.setRole(Role.ADMIN);

            // Cria o usuário no banco de dados
            userService.registerUser(admin.getName(), admin.getEmail(), admin.getPassword(), admin.getRole());
            System.out.println("Admin user created successfully.");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
