package com.celia.auth_service.clients;

import com.celia.auth_service.configs.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import feign.Headers;
import java.util.Map;

@FeignClient(
        name = "keycloak-client",
        url = "${app.keycloak.server-url}/realms/${app.keycloak.realm}/protocol/openid-connect",
        configuration = FeignConfig.class // Apply Feign Form Encoder
)
public interface KeycloakFeignClient {

    @PostMapping(value = "/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Map<String, Object> refreshAccessToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("grant_type") String grantType,
            @RequestParam("refresh_token") String refreshToken
    );
}