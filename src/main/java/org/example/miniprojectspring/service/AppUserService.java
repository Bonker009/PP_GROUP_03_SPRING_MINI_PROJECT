package org.example.miniprojectspring.service;

import org.example.miniprojectspring.model.entity.AppUser;
import org.example.miniprojectspring.model.dto.AppUserDTO;
import org.example.miniprojectspring.model.request.AppUserRequest;
import org.example.miniprojectspring.model.request.PasswordRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface AppUserService extends UserDetailsService {
    AppUserDTO createUser(AppUserRequest appUserRequest);
    AppUserDTO findUserByEmail(String email);

}
