package com.vendorshield.config;

import com.vendorshield.domain.model.User;
import com.vendorshield.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            createUserIfNotFound(userRepository, passwordEncoder, "admin", "admin123", "ROLE_ADMIN");
            createUserIfNotFound(userRepository, passwordEncoder, "manager", "manager123", "ROLE_MANAGER");
            createUserIfNotFound(userRepository, passwordEncoder, "viewer", "viewer123", "ROLE_VIEWER");
        };
    }

    private void createUserIfNotFound(UserRepository userRepository,
                                    PasswordEncoder passwordEncoder,
                                    String username,
                                    String rawPassword,
                                    String role) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setRole(role);
            user.setEnabled(true);
            userRepository.save(user);
            log.info("Initialized default user: '{}' with role: '{}'", username, role);
        }
    }
}
