package org.example.miniprojectspring.service.ServiceImplementation;

import lombok.AllArgsConstructor;
import org.example.miniprojectspring.model.dto.OptsDTO;
import org.example.miniprojectspring.model.entity.AppUser;
import org.example.miniprojectspring.model.dto.AppUserDTO;
import org.example.miniprojectspring.model.entity.CustomUserDetail;
import org.example.miniprojectspring.model.request.AppUserRequest;
import org.example.miniprojectspring.model.request.PasswordRequest;
import org.example.miniprojectspring.repository.AppUserRepository;
import org.example.miniprojectspring.service.AppUserService;
import org.example.miniprojectspring.service.OptService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private final OptService optService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUserDTO appUserDTO = appUserRepository.findByEmail(email);
        return new CustomUserDetail(appUserDTO);
    }

    @Override
    public AppUserDTO createUser(AppUserRequest appUserRequest) {
        return appUserRepository.saveUser(appUserRequest);
    }

    @Override
    public AppUserDTO findUserByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }


}