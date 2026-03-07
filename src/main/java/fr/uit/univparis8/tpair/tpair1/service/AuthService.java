package fr.uit.univparis8.tpair.tpair1.service;

import fr.uit.univparis8.tpair.tpair1.model.User;
import fr.uit.univparis8.tpair.tpair1.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import fr.uit.univparis8.tpair.tpair1.enums.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public User authenticate(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .filter(u -> passwordEncoder.matches(rawPassword, u.getPassword()))
                .orElse(null);
    }

    public User register(String username, String email, String rawPassword) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username obligatoire");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email obligatoire");
        if (rawPassword == null || rawPassword.isBlank()) throw new IllegalArgumentException("Password obligatoire");

        userRepository.findByUsername(username).ifPresent(u -> {
            throw new IllegalArgumentException("Username déjà utilisé");
        });
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new IllegalArgumentException("Email déjà utilisé");
        });

        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRole(Role.USER);

        return userRepository.save(u);
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public org.springframework.security.core.userdetails.UserDetails loadUser(String username) {
        return userRepository.findByUsername(username)
                .map(u -> org.springframework.security.core.userdetails.User
                        .withUsername(u.getUsername())
                        .password(u.getPassword())
                        .authorities("ROLE_" + u.getRole().name())
                        .build())
                .orElse(null);
    }
}
