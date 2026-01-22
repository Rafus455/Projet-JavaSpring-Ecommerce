package com.b2.e_commerce.controller;

import com.b2.e_commerce.DTO.LoginRequest;
import com.b2.e_commerce.DTO.RegisterRequest;
import com.b2.e_commerce.DTO.LoginResponse;
import com.b2.e_commerce.entity.Role;
import com.b2.e_commerce.entity.User;
import com.b2.e_commerce.repository.RoleRepository;
import com.b2.e_commerce.repository.UserRepository;
import com.b2.e_commerce.security.JwtUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthController(
            AuthenticationManager authManager,
            UserRepository userRepo,
            RoleRepository roleRepo,
            PasswordEncoder encoder,
            JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    // LOGIN JWT
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        String token = jwtUtil.generateToken(auth.getName());

        return ResponseEntity.ok(new LoginResponse(token));
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {

        if (userRepo.findByMail(req.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        Role role = roleRepo.findByRole("USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));

        User u = new User();
        u.setMail(req.getEmail());
        u.setPassword(encoder.encode(req.getPassword()));
        u.setRole(role);

        userRepo.save(u);

        return ResponseEntity.ok().build();
    }
}


