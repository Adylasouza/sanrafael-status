package dev.adylaoliveira.sanrafael.adapter.controller;

import dev.adylaoliveira.sanrafael.core.exception.NotFoundException;
import dev.adylaoliveira.sanrafael.core.security.JwtService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {

        String username = request.get("username");
        String password = request.get("password");

        // LOGIN SIMPLES (para projeto acadêmico)
        if ("admin".equals(username) && "123".equals(password)) {
            String token = jwtService.generateToken(username);
            return Map.of("token", token);
        }

        throw new NotFoundException("Invalid credentials");
    }
}