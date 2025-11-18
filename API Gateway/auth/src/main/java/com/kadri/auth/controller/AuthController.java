package com.kadri.auth.controller;

import com.kadri.auth.dto.AuthResponse;
import com.kadri.auth.dto.LoginRequest;
import com.kadri.auth.dto.RegisterRequest;
import com.kadri.auth.model.AuthUser;
import com.kadri.auth.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expirationSeconds;
    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request){
        service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        //validate username and password against DB
        AuthUser user = service.authenticate(request.getUsername(), request.getPassword());

        //Build JWT token for this user
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationSeconds * 1000);

        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("roles", List.of("ROLE_USER"))
                .signWith(
                        Keys.hmacShaKeyFor(secret.getBytes(UTF_8)),
                        SignatureAlgorithm.HS512
                )
                .compact();
        return ResponseEntity.ok(new AuthResponse(token, "Bearer"));
    }
}
