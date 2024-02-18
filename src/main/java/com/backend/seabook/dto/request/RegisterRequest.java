package com.backend.seabook.dto.request;

import com.backend.seabook.validation.FieldExistence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank
    private String name;

    @NotBlank
    @Email
    @FieldExistence(tableName = "users", fieldName = "email", shouldExist = false, message = "Email already exists")
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;
}
