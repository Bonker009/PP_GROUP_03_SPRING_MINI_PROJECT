package org.example.miniprojectspring.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseRequest {
    @Positive(message = "Amount should be empty and not zero too.")
    private double amount;
    @NotBlank(message = "Description should not be blank")
    @NotNull(message = "Description should be empty")
    private String description;
    private Date date;
    @NotNull(message = "AppUser Id should be empty")
    private UUID categoryId;
}
