package org.example.miniprojectspring.service.ServiceImplementation;

import lombok.Data;
import org.example.miniprojectspring.model.dto.OptsDTO;
import org.example.miniprojectspring.model.request.PasswordRequest;
import org.example.miniprojectspring.repository.AppUserRepository;
import org.example.miniprojectspring.repository.OneTimePasswordRepository;
import org.example.miniprojectspring.service.OptService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data

public class OptServiceImpl implements OptService {
    private String verificationResult;
    private final OneTimePasswordRepository oneTimePasswordRepository;
    private final AppUserRepository appUserRepository;

    public OptServiceImpl(OneTimePasswordRepository oneTimePasswordRepository, AppUserRepository appUserRepository) {
        this.oneTimePasswordRepository = oneTimePasswordRepository;
        this.appUserRepository = appUserRepository;
    }

    @Override
    public Optional<OptsDTO> findById(Integer id) {
        return oneTimePasswordRepository.findById(id);
    }

    @Override
    public void save(OptsDTO optsDTO) {
        oneTimePasswordRepository.createNewOpt(optsDTO);
    }

    @Override
    public Optional<OptsDTO> findByCode(String code) {
        return Optional.ofNullable(oneTimePasswordRepository.findByCode(code));
    }

    @Override
    public void uploadOpt(String optCode) {
        oneTimePasswordRepository.save(optCode);
    }

    @Override
    public void verify(String optCode) {
        oneTimePasswordRepository.verify(optCode);
    }

    @Override
    public void resendOpt(String email) {
//        oneTimePasswordRepository.
    }

    @Override
    public void resetPasswordByEmail(PasswordRequest passwordRequest, String email) {
            appUserRepository.resetPasswordById(passwordRequest,email);
    }


}
