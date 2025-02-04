package com.celia.auth_service.configs;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Value("${app.keycloak.server-url}")
    private String keycloak_server_url;

    @Value("${app.keycloak.admin-realm}")
    private String admin_realm;

    @Value("${app.keycloak.admin-client-id}")
    private String admin_client_id;

    @Value("${app.keycloak.admin-username}")
    private String admin_username;

    @Value("${app.keycloak.admin-password}")
    private String admin_password;

    @Bean
    public Keycloak keycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloak_server_url)
                .realm(admin_realm)
                .clientId(admin_client_id)
                .grantType(OAuth2Constants.PASSWORD)
                .username(admin_username)
                .password(admin_password)
                .build();
    }
}