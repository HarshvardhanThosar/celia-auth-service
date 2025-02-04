package com.celia.auth_service.controllers;
import com.celia.auth_service.services.CommunityAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class CommunityAuthController {
    private final CommunityAuthService userService;

    public CommunityAuthController(CommunityAuthService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password) {

        System.out.println("Registering user: " + username);
        String result = userService.createUser(username, email, password);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint.";
    }

    @GetMapping("/private")
    public Map<String, Object> privateEndpoint(@AuthenticationPrincipal Jwt jwt) {
        return jwt.getClaims();
    }
}