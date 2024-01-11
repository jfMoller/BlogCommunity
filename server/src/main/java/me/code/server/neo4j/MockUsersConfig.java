package me.code.server.neo4j;

import me.code.server.model.User;
import me.code.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MockUsersConfig {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final List<User> MOCK_USERS = List.of(
            new User(
                    "user",
                    "password"));

    private static final List<User> MOCK_ADMINS = List.of(
            new User(
                    "admin",
                    "password"));

    @Autowired
    public MockUsersConfig(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void createDefaultUsers() {
        if (isUserRepositoryEmpty()) {
            MOCK_USERS.forEach(this::saveMockUser);
            MOCK_ADMINS.forEach(this::saveMockAdmin);
        }
    }

    public boolean isUserRepositoryEmpty() {
        return userRepository.count() == 0;
    }

    private void saveMockUser(User user) {
        try {
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);
            userRepository.save(user);

            System.out.println("MockUsersConfig created a new user: " + user.getUsername());

        } catch (Exception exception) {
            System.out.println("MockUsersConfig failed to create a new user: " + exception.getMessage());
        }
    }

    private void saveMockAdmin(User user) {
        try {
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);
            user.setRole(User.Role.ADMIN);
            userRepository.save(user);

            System.out.println("MockUsersConfig created a new admin: " + user.getUsername());

        } catch (Exception exception) {
            System.out.println("MockUsersConfig failed to create a new admin " + exception.getMessage());
        }
    }

}
