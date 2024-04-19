package org.example.miniprojectspring.service;

import org.example.miniprojectspring.model.dto.OptsDTO;
import org.example.miniprojectspring.model.request.PasswordRequest;

import java.util.Optional;

public interface OptService {
    Optional<OptsDTO> findById(Integer id);

    void save(OptsDTO optsDTO);
    Optional<OptsDTO> findByCode(String code);
    void uploadOpt(String optCode);
    void verify(String optCode);
    void resendOpt(String email);
    void resetPasswordByEmail(PasswordRequest passwordRequest , String email);


}
