package com.celia.auth_service.services.implimentations;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.celia.auth_service.dtos.requests.RefreshTokenRequestDTO;
import com.celia.auth_service.dtos.responses.CommunityUserResponseDTO;
import com.celia.auth_service.dtos.requests.LoginCommunityUserRequestDTO;
import com.celia.auth_service.dtos.requests.RegisterCommunityUserRequestDTO;
import com.celia.auth_service.dtos.responses.ResponseBodyDTO;
import com.celia.auth_service.services.CommunityAuthServiceInterface;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommunityAuthService implements CommunityAuthServiceInterface {

    private final Keycloak keycloak;

    @Value("${app.keycloak.realm}")
    private String keycloak_realm;

    @Value("${app.keycloak.server-url}")
    private String keycloak_server_url;

    @Value("${app.keycloak.client-id}")
    private String keycloak_client_id;

    @Value("${app.keycloak.client-secret}")
    private String keycloak_client_secret;

    /// Registers a new community user for the application
    ///
    /// @param register_community_user_request_dto RegisterCommunityUserRequestDTO
    /// @return response ResponseEntity<ResponseBodyDTO<CommunityUserResponseDTO>>
    @Override
    public ResponseEntity<ResponseBodyDTO<CommunityUserResponseDTO>> register_new_community_user(RegisterCommunityUserRequestDTO register_community_user_request_dto) {
        String email = register_community_user_request_dto.email();
        UserRepresentation user_representation = get_user_representation(register_community_user_request_dto, email);

        // Access the user resource to create new users
        UsersResource _users_resource = get_user_resource();

        try (Response response = _users_resource.create(user_representation)) {
            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                String success_message = "User registered successfully!";
                int status = response.getStatus();
                ResponseBodyDTO<CommunityUserResponseDTO> response_body = new ResponseBodyDTO<>(success_message, null, status, null);
                log.info("User registered successfully!: Email: {} | Status: {} | Response {}", email, status, success_message);
                return ResponseEntity.status(response.getStatus()).body(response_body);
            } else {
                String error_message = response.readEntity(String.class);
                int status = response.getStatus();
                log.error("Failed to create user! Email: {} | Status: {} | Response: {}", email, status, error_message);
                return ResponseEntity.status(status).body(new ResponseBodyDTO<>(error_message, null, status, null));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /// Logs in an existing user for the application
    ///
    /// @param login_community_user_request_dto LoginCommunityUserRequestDTO
    /// @return ResponseEntity<ResponseBodyDTO <AccessTokenResponse>>
    @Override
    public ResponseEntity<ResponseBodyDTO<AccessTokenResponse>> login_community_user(LoginCommunityUserRequestDTO login_community_user_request_dto) {
        String username = login_community_user_request_dto.username();
        String password = login_community_user_request_dto.password();

        try {
            AccessTokenResponse tokenResponse = authenticate_user(username, password);
            String message = "User logged-in successfully!!";
            ResponseBodyDTO<AccessTokenResponse> responseBody = new ResponseBodyDTO<>(message, tokenResponse, HttpStatus.OK.value(), null);
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            log.error("Failed to authenticate user: {} | Error: {}", username, e.getMessage());
            String errorMessage = "Invalid credentials or failed authentication.";
            ResponseBodyDTO<AccessTokenResponse> responseBody = new ResponseBodyDTO<>(errorMessage, null, HttpStatus.UNAUTHORIZED.value(), null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }
    }

    /// Refresh token for existing logged-in sessions
    ///
    /// @param refresh_token_request_dto RefreshTokenRequestDTO
    /// @return ResponseEntity<ResponseBodyDTO < AccessTokenResponse>>
    @Override
    public ResponseEntity<ResponseBodyDTO<AccessTokenResponse>> refresh_token(RefreshTokenRequestDTO refresh_token_request_dto) {
        try {
            AccessTokenResponse tokenResponse = refresh_access_token(refresh_token_request_dto);
            String message = "Token refreshed successfully!!";
            ResponseBodyDTO<AccessTokenResponse> responseBody = new ResponseBodyDTO<>(message, tokenResponse, HttpStatus.OK.value(), null);
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            log.error("Failed to refresh token for user: {} | Error: {}", null, e.getMessage());
            String errorMessage = "Invalid credentials or failed authentication.";
            ResponseBodyDTO<AccessTokenResponse> responseBody = new ResponseBodyDTO<>(errorMessage, null, HttpStatus.UNAUTHORIZED.value(), null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }
    }

    private AccessTokenResponse refresh_access_token(RefreshTokenRequestDTO refresh_token_request_dto) {
        // Keycloak token endpoint URL
        String refresh_token = refresh_token_request_dto.refresh_token();
        String tokenUrl = keycloak_server_url + "/realms/" + keycloak_realm + "/protocol/openid-connect/token";

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Set up request body
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", keycloak_client_id);
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("refresh_token", refresh_token);

        // Create the HTTP entity
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Make the HTTP POST request
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(tokenUrl, requestEntity, AccessTokenResponse.class);
    }

    private AccessTokenResponse authenticate_user(String username, String password) {
        Keycloak keycloakAuth = KeycloakBuilder.builder()
                .serverUrl(keycloak_server_url)
                .realm(keycloak_realm)
                .clientId(keycloak_client_id)
                .grantType(OAuth2Constants.PASSWORD)
                .username(username)
                .password(password)
                .build();

        return keycloakAuth.tokenManager().getAccessToken();
    }

    private static UserRepresentation get_user_representation(RegisterCommunityUserRequestDTO register_community_user_request_dto, String email) {
        String password = register_community_user_request_dto.password();
        String first_name = register_community_user_request_dto.first_name();
        String last_name = register_community_user_request_dto.last_name();

        UserRepresentation user_representation = new UserRepresentation();

        // Setting up user details
        user_representation.setFirstName(first_name);
        user_representation.setLastName(last_name);

        // TODO: Give user the freedom to customize username by providing another API
        // NOTE: Using email as username for this POC
        user_representation.setUsername(email);

        // Setting up email
        user_representation.setEmail(email);
        user_representation.setEmailVerified(true);

        // Setting up active state for the user
        user_representation.setEnabled(true);

        // Setting up user credentials
        CredentialRepresentation credential_representation = new CredentialRepresentation();
        credential_representation.setType(CredentialRepresentation.PASSWORD);
        credential_representation.setValue(password);
        user_representation.setCredentials(Collections.singletonList(credential_representation));
        return user_representation;
    }

    private UsersResource get_user_resource() {
        return keycloak.realm(keycloak_realm).users();
    }

}