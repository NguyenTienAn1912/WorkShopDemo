package com.example.ratingservice.config;

import com.example.ratingservice.entity.Item;
import com.example.ratingservice.entity.User;
import com.example.ratingservice.repository.ItemRepository;
import com.example.ratingservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create sample users
        if (!userRepository.existsByUsername("user1")) {
            User user1 = new User("user1", passwordEncoder.encode("password"), "user1@example.com", "John Doe");
            user1.setRole(User.Role.USER);
            userRepository.save(user1);
        }

        if (!userRepository.existsByUsername("user2")) {
            User user2 = new User("user2", passwordEncoder.encode("password"), "user2@example.com", "Jane Smith");
            user2.setRole(User.Role.USER);
            userRepository.save(user2);
        }

        if (!userRepository.existsByUsername("admin")) {
            User admin = new User("admin", passwordEncoder.encode("admin"), "admin@example.com", "Admin User");
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
        }

        // Create sample items
        if (itemRepository.count() == 0) {
            Item item1 = new Item("iPhone 15", "Latest Apple smartphone with advanced features", new BigDecimal("999.99"));
            itemRepository.save(item1);

            Item item2 = new Item("Samsung Galaxy S24", "High-end Android smartphone", new BigDecimal("899.99"));
            itemRepository.save(item2);

            Item item3 = new Item("MacBook Pro", "Professional laptop for developers", new BigDecimal("1999.99"));
            itemRepository.save(item3);

            Item item4 = new Item("Dell XPS 13", "Compact and powerful ultrabook", new BigDecimal("1299.99"));
            itemRepository.save(item4);

            Item item5 = new Item("AirPods Pro", "Wireless earbuds with noise cancellation", new BigDecimal("249.99"));
            itemRepository.save(item5);
        }

        System.out.println("Sample data initialized!");
        System.out.println("Test users:");
        System.out.println("- Username: user1, Password: password (USER role)");
        System.out.println("- Username: user2, Password: password (USER role)");
        System.out.println("- Username: admin, Password: admin (ADMIN role)");
    }
}