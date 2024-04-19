package org.example.miniprojectspring.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseRequest {
    private double amount;
    private String description;
    private Date date;
    private UUID categoryId;
}
