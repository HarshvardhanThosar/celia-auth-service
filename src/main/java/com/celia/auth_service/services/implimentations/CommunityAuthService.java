package com.celia.auth_service.services.implimentations;

import com.celia.auth_service.dtos.CommunityUserDTO;
import com.celia.auth_service.dtos.RegisterCommunityUserDTO;
import com.celia.auth_service.dtos.ResponseBodyDTO;
import com.celia.auth_service.services.CommunityAuthServiceInterface;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;

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

    /// Registers a new community user for the application
    ///
    /// @param register_community_user_dto RegisterCommunityUserDTO
    /// @return response ResponseEntity<ResponseBodyDTO<CommunityUserDTO>>
    @Override
    public ResponseEntity<ResponseBodyDTO<CommunityUserDTO>> register_new_community_user(RegisterCommunityUserDTO register_community_user_dto) {
        String email = register_community_user_dto.email();
        UserRepresentation user_representation = get_user_representation(register_community_user_dto, email);

        // Access the user resource to create new users
        UsersResource _users_resource = get_user_resource();

        try (Response response = _users_resource.create(user_representation)) {
            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                String success_message = "User registered successfully!";
                int status = response.getStatus();
                ResponseBodyDTO<CommunityUserDTO> response_body = new ResponseBodyDTO<>(success_message, null, status, null);
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

    private static UserRepresentation get_user_representation(RegisterCommunityUserDTO register_community_user_dto, String email) {
        String password = register_community_user_dto.password();
        String first_name = register_community_user_dto.first_name();
        String last_name = register_community_user_dto.last_name();

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