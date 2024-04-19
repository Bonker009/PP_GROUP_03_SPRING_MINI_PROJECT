package org.example.miniprojectspring.service;

import jakarta.mail.MessagingException;
import org.example.miniprojectspring.model.dto.OptsDTO;
import org.example.miniprojectspring.model.request.PasswordRequest;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface OptService {
    Optional<OptsDTO> findById(Integer id);

    void save(OptsDTO optsDTO);
    OptsDTO findByCode(String code);
    void uploadOpt(String optCode);
    void verify(String optCode);
    void resendOpt(String email) throws MessagingException, IOException;
    void resetPasswordByEmail(PasswordRequest passwordRequest , String email);
    OptsDTO findOptByUserId(UUID userId);


}
