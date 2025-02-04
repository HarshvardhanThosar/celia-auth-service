package com.celia.auth_service.controllers;

import com.celia.auth_service.dtos.*;
import com.celia.auth_service.services.implimentations.CommunityAuthService;
import jakarta.ws.rs.core.Response;
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
@RequestMapping("/community")
public class CommunityAuthController {

    private final CommunityAuthService community_authService;

    public CommunityAuthController(CommunityAuthService communityAuthService) {
        community_authService = communityAuthService;
    }

    @PostMapping("/users/register")
    public ResponseEntity<ResponseBodyDTO<CommunityUserDTO>> register_new_community_user(@RequestBody RegisterCommunityUserDTO _new_community_user_dto) {
        return community_authService.register_new_community_user(_new_community_user_dto);
    }

//    @PostMapping("/users/register")
//    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
//        UserRepresentation user = new UserRepresentation();
//        user.setUsername(userDto.getUsername());
//        user.setFirstName(userDto.getFirstName());
//        user.setLastName(userDto.getLastName());
//        user.setEmail(userDto.getEmail());
//        user.setEnabled(true);
//
//        CredentialRepresentation credential = new CredentialRepresentation();
//        credential.setType(CredentialRepresentation.PASSWORD);
//        credential.setValue(userDto.getPassword());
//        credential.setTemporary(false);
//
//        user.setCredentials(Collections.singletonList(credential));
//
//        Response response = keycloak.realm(keycloak_realm).users().create(user);
//
//        if (response.getStatus() == 201) {
//            return ResponseEntity.ok("User created successfully");
//        } else {
//            return ResponseEntity.status(response.getStatus()).body("Failed to create user");
//        }
//    }
//
//    @PostMapping("/users/login")
//    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
//        try {
//            Keycloak keycloak = KeycloakBuilder.builder()
//                    .serverUrl(keycloak_server_url)
//                    .realm(keycloak_realm)
//                    .clientId(keycloak_client_id)
//                    .grantType(OAuth2Constants.PASSWORD)
//                    .username(loginDto.getUsername())
//                    .password(loginDto.getPassword())
//                    .build();
//
//            AccessTokenResponse tokenResponse = keycloak.tokenManager().getAccessToken();
//            return ResponseEntity.ok(tokenResponse);
//        } catch (Exception e) {
//            return ResponseEntity.status(401).body("Invalid credentials");
//        }
//    }
//
//    @GetMapping("/users/")
//    public ResponseEntity<UserDto> getUser() {
//        return null;
//    }
}