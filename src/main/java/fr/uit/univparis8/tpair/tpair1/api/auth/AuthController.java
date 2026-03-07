package fr.uit.univparis8.tpair.tpair1.api.auth;

import fr.uit.univparis8.tpair.tpair1.api.auth.dto.LoginRequest;
import fr.uit.univparis8.tpair.tpair1.api.auth.dto.LoginResponse;
import fr.uit.univparis8.tpair.tpair1.api.auth.dto.RegisterRequest;
import fr.uit.univparis8.tpair.tpair1.model.User;
import fr.uit.univparis8.tpair.tpair1.service.AuthService;
import fr.uit.univparis8.tpair.tpair1.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Validated @RequestBody RegisterRequest req) {
        authService.register(req.getUsername(), req.getEmail(), req.getPassword());
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        User u = authService.authenticate(req.getUsername(), req.getPassword());
        if (u == null) return ResponseEntity.status(401).build();
        String token = jwtService.generateToken(u.getUsername(), Map.of(
                "userId", u.getId(),
                "role", "ROLE_" + u.getRole().name()
        ));
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
