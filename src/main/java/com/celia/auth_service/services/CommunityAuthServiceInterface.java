package com.celia.auth_service.services;

import com.celia.auth_service.dtos.CommunityUserDTO;
import com.celia.auth_service.dtos.LoginCommunityUserDTO;
import com.celia.auth_service.dtos.RegisterCommunityUserDTO;
import com.celia.auth_service.dtos.ResponseBodyDTO;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;

public interface CommunityAuthServiceInterface {

    /// Registers a new community user for the application
    ///
    /// @param register_community_user_dto RegisterCommunityUserDTO
    /// @return response ResponseEntity<ResponseBodyDTO<CommunityUserDTO>>
    ResponseEntity<ResponseBodyDTO<CommunityUserDTO>> register_new_community_user(RegisterCommunityUserDTO register_community_user_dto);

    /// Logs in an existing user for the application
    ///
    /// @param login_community_user_dto LoginCommunityUserDTO
    /// @return ResponseEntity<ResponseBodyDTO<AccessTokenResponse>>
    ResponseEntity<ResponseBodyDTO<AccessTokenResponse>> login_community_user(LoginCommunityUserDTO login_community_user_dto);

}
