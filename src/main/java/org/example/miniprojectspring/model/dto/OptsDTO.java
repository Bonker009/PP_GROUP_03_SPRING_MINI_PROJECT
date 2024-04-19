package org.example.miniprojectspring.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptsDTO {
    private String optCode;
    private Date issuedDate;
    private Date expiration;
    private boolean verify;
    private UUID userId;
}
