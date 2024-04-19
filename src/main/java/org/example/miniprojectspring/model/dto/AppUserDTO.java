package org.example.miniprojectspring.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTO{
    private UUID userId;
    private String email;
//    @JsonIgnore
    private String password;
    private String profileImage;
}
