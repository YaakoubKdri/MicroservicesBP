package com.kadri.auth.service;

import com.kadri.auth.dto.RegisterRequest;
import com.kadri.auth.model.AuthUser;
import com.kadri.auth.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthUser register(RegisterRequest request) {
        if(repository.findByUsername(request.getUsername()).isPresent()){
            throw new IllegalArgumentException("Username already taken.");
        }
        if(repository.findByEmail(request.getEmail()).isPresent()){
            throw new IllegalArgumentException("Email already used.");
        }

        var user = new AuthUser();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        return repository.save(user);
    }

    public AuthUser authenticate(String username, String rawPassword){
        var user = repository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if(!passwordEncoder.matches(rawPassword, user.getPasswordHash())){
            throw new IllegalArgumentException("Invalid credentials");
        }
        return user;
    }
}
