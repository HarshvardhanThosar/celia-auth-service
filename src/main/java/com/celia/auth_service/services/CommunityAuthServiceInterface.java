package com.celia.auth_service.services;

import com.celia.auth_service.dtos.CommunityUserDTO;
import com.celia.auth_service.dtos.RegisterCommunityUserDTO;
import com.celia.auth_service.dtos.ResponseBodyDTO;
import org.springframework.http.ResponseEntity;

public interface CommunityAuthServiceInterface {

    /// Registers a new community user for the application
    ///
    /// @param register_community_user_dto RegisterCommunityUserDTO
    /// @return response ResponseEntity<ResponseBodyDTO<CommunityUserDTO>>
    ResponseEntity<ResponseBodyDTO<CommunityUserDTO>> register_new_community_user(RegisterCommunityUserDTO register_community_user_dto);

}
