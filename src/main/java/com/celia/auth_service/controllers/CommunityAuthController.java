package com.celia.auth_service.controllers;

import com.celia.auth_service.dtos.requests.LogOutRequestDTO;
import com.celia.auth_service.dtos.requests.LoginCommunityUserRequestDTO;
import com.celia.auth_service.dtos.requests.RefreshTokenRequestDTO;
import com.celia.auth_service.dtos.requests.RegisterCommunityUserRequestDTO;
import com.celia.auth_service.dtos.responses.CommunityUserResponseDTO;
import com.celia.auth_service.dtos.responses.ResponseBodyDTO;
import com.celia.auth_service.services.implimentations.CommunityAuthService;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/community")
public class CommunityAuthController {

    private final CommunityAuthService community_authService;

    public CommunityAuthController(CommunityAuthService _community_authService) {
        community_authService = _community_authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseBodyDTO<CommunityUserResponseDTO>> register_new_community_user(@RequestBody RegisterCommunityUserRequestDTO _new_community_user_request_dto) {
        return community_authService.register_new_community_user(_new_community_user_request_dto);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseBodyDTO<AccessTokenResponse>> login_community_user(@RequestBody LoginCommunityUserRequestDTO _login_community_user_request_dto) {
        return community_authService.login_community_user(_login_community_user_request_dto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseBodyDTO<AccessTokenResponse>> refresh_token(@RequestBody RefreshTokenRequestDTO _refresh_token) {
        return community_authService.refresh_token(_refresh_token);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseBodyDTO<String>> logout(@RequestBody LogOutRequestDTO _log_out_request_dto) {
        return community_authService.log_out(_log_out_request_dto);

    }

    @PostMapping("/validate")
    public ResponseEntity<ResponseBodyDTO<Boolean>> validate_token(@RequestHeader("Authorization") String access_token) {

        return null;
    }

}