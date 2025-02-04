package com.celia.auth_service.services;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import jakarta.ws.rs.core.Response;
import java.util.Collections;

@Service
public class CommunityAuthService {

    private final Keycloak keycloak;

    public CommunityAuthService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public String createUser(String username, String email, String password) {
        RealmResource realmResource = keycloak.realm("celia-auth");
        UsersResource usersResource = realmResource.users();

        // Define user credentials
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        // Define user representation
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);
        user.setCredentials(Collections.singletonList(credential));

        // Create user in Keycloak
        Response response = usersResource.create(user);

        // Log response for debugging
        System.out.println("Response Status: " + response.getStatus());
        System.out.println("Response Body: " + response.readEntity(String.class));

        if (response.getStatus() == 201) {
            return "User created successfully!";
        } else {
            return "Failed to create user: " + response.getStatus();
        }
    }
}