package org.example.miniprojectspring.service.ServiceImplementation;

import jakarta.mail.MessagingException;
import lombok.Data;
import org.example.miniprojectspring.model.dto.AppUserDTO;
import org.example.miniprojectspring.model.dto.OptsDTO;
import org.example.miniprojectspring.model.request.PasswordRequest;
import org.example.miniprojectspring.repository.AppUserRepository;
import org.example.miniprojectspring.repository.OneTimePasswordRepository;
import org.example.miniprojectspring.service.EmailService;
import org.example.miniprojectspring.service.OptGenerator;
import org.example.miniprojectspring.service.OptService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@Data
public class OptServiceImpl implements OptService {
    private String verificationResult;
    private final OneTimePasswordRepository oneTimePasswordRepository;
    private final AppUserRepository appUserRepository;
    private final EmailService emailService;


    public OptServiceImpl(OneTimePasswordRepository oneTimePasswordRepository, AppUserRepository appUserRepository, EmailService emailService) {
        this.oneTimePasswordRepository = oneTimePasswordRepository;
        this.appUserRepository = appUserRepository;
        this.emailService = emailService;

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
    public OptsDTO findByCode(String code) {
        return oneTimePasswordRepository.findByCode(code);
    }

    @Override
    public void uploadOpt(String optCode) {
        oneTimePasswordRepository.save(optCode);
    }

    @Override
    public void verify(String optCode) {
        System.out.println(optCode);
        oneTimePasswordRepository.verify(optCode);
    }



    @Override
    public void resendOpt(String email) throws MessagingException, IOException {
        AppUserDTO appUserDTO = appUserRepository.findByEmail(email);
        OptsDTO optsDTO = OptGenerator.generateOTP(6,appUserDTO.getUserId());
        oneTimePasswordRepository.createNewOpt(optsDTO);
        String optsGenerated = emailService.sendEmail(email, optsDTO.getOptCode());
        System.out.println(optsGenerated);
    }

    @Override
    public void resetPasswordByEmail(PasswordRequest passwordRequest, String email) {
            appUserRepository.resetPasswordById(passwordRequest,email);
    }

    @Override
    public OptsDTO findOptByUserId(UUID userId) {
        return oneTimePasswordRepository.findOptByUserId(userId);
    }




}
