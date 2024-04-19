package org.example.miniprojectspring.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Opts {
    private UUID optId;
    private String optCode;
    private Date issuedDate;
    private Date expiration;
    private String verify;
    private UUID userId;
}
