package com.celia.auth_service.controllers;

import com.celia.auth_service.dtos.LoginDto;
import com.celia.auth_service.dtos.UserDto;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class CommunityAuthController {

    private final Keycloak keycloak;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userDto.getPassword());
        credential.setTemporary(false);

        user.setCredentials(Collections.singletonList(credential));

        Response response = keycloak.realm("celia-auth-realm").users().create(user);

        if (response.getStatus() == 201) {
            return ResponseEntity.ok("User created successfully");
        } else {
            return ResponseEntity.status(response.getStatus()).body("Failed to create user");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl("http://localhost:8080")
                    .realm("celia-auth-realm")
                    .clientId("celia-auth-client")
                    .grantType(OAuth2Constants.PASSWORD)
                    .username(loginDto.getUsername())
                    .password(loginDto.getPassword())
                    .build();

            AccessTokenResponse tokenResponse = keycloak.tokenManager().getAccessToken();
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}