package org.example.miniprojectspring.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.miniprojectspring.model.dto.AppUserDTO;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
    private UUID expenseId;
    private double amount;
    private String description;
    private Date date;
    private AppUserDTO appUserDTO;
    private Category category;
}
