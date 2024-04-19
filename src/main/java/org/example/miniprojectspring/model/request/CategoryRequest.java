package org.example.miniprojectspring.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    @NotNull(message = "Name should be empty")
    @NotBlank(message = "Name should not be blank")
    private String name;
    @NotNull(message = "Description should be empty")
    @NotBlank(message = "Description should not be blank")
    private String description;
}
