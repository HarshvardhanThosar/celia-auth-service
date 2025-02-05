
package com.celia.auth_service.services;

import com.celia.auth_service.dtos.requests.LogOutRequestDTO;
import com.celia.auth_service.dtos.requests.RefreshTokenRequestDTO;
import com.celia.auth_service.dtos.responses.CommunityUserResponseDTO;
import com.celia.auth_service.dtos.requests.LoginCommunityUserRequestDTO;
import com.celia.auth_service.dtos.requests.RegisterCommunityUserRequestDTO;
import com.celia.auth_service.dtos.responses.ResponseBodyDTO;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;

public interface CommunityAuthServiceInterface {

    /// Registers a new community user for the application
    ///
    /// @param register_community_user_request_dto RegisterCommunityUserRequestDTO
    /// @return response ResponseEntity<ResponseBodyDTO<CommunityUserResponseDTO>>
    ResponseEntity<ResponseBodyDTO<CommunityUserResponseDTO>> register_new_community_user(RegisterCommunityUserRequestDTO register_community_user_request_dto);

    /// Logs in an existing user for the application
    ///
    /// @param login_community_user_request_dto LoginCommunityUserRequestDTO
    /// @return ResponseEntity<ResponseBodyDTO<AccessTokenResponse>>
    ResponseEntity<ResponseBodyDTO<AccessTokenResponse>> login_community_user(LoginCommunityUserRequestDTO login_community_user_request_dto);

    /// Refresh token for existing logged-in sessions
    ///
    /// @param refresh_token_request_dto RefreshTokenRequestDTO
    /// @return ResponseEntity<ResponseBodyDTO<AccessTokenResponse>>
    ResponseEntity<ResponseBodyDTO<AccessTokenResponse>> refresh_token(RefreshTokenRequestDTO refresh_token_request_dto);


    /// Log out an existing session
    ///
    /// @param log_out_request_dto LogOutRequestDTO
    /// @return ResponseEntity<ResponseBodyDTO<String>>
    ResponseEntity<ResponseBodyDTO<String>> log_out(LogOutRequestDTO log_out_request_dto);

    /// Validate an access token
    ///
    /// @param access_token String
    /// @return ResponseEntity<ResponseBodyDTO<Boolean>>
    ResponseEntity<ResponseBodyDTO<Boolean>> validate_token(String access_token);

}
